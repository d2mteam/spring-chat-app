const apiBase =
  window.location.protocol === "file:" ? "http://localhost:8080/api" : "/api";

const statusEl = document.getElementById("status");
const roomTitleEl = document.getElementById("roomTitle");
const messageListEl = document.getElementById("messageList");
const refreshMessagesBtn = document.getElementById("refreshMessages");
const toggleSliderBtn = document.getElementById("toggleSlider");
const sendMessageForm = document.getElementById("sendMessageForm");
const messageInput = document.getElementById("messageInput");

let currentRoom = null;
let messages = [];
let isSliderMode = false;
let socket = null;

const setStatus = (message, isError = false) => {
  statusEl.textContent = message;
  statusEl.style.background = isError ? "#fee2e2" : "#e2e8f0";
  statusEl.style.color = isError ? "#991b1b" : "#0f172a";
};

const getErrorMessage = async (response, fallbackMessage) => {
  if (!response) {
    return fallbackMessage;
  }

  try {
    const data = await response.clone().json();
    if (data?.message) {
      return data.message;
    }
  } catch (error) {
    // Ignore JSON parsing errors.
  }

  try {
    const text = await response.text();
    if (text) {
      return text;
    }
  } catch (error) {
    // Ignore text parsing errors.
  }

  return fallbackMessage;
};

const formatDate = (value) => {
  if (!value) return "";
  const date = new Date(value);
  return new Intl.DateTimeFormat("vi-VN", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(date);
};

const renderMessages = (nextMessages) => {
  messageListEl.innerHTML = "";
  messageListEl.classList.toggle("message-list--slider", isSliderMode);
  if (nextMessages.length === 0) {
    messageListEl.innerHTML = "<li>Phòng này chưa có tin nhắn.</li>";
    return;
  }

  nextMessages.forEach((message) => {
    appendMessage(message);
  });
};

const appendMessage = (message) => {
  if (!message) return;
  if (messageListEl.firstElementChild?.textContent === "Phòng này chưa có tin nhắn.") {
    messageListEl.innerHTML = "";
  }
  const li = document.createElement("li");
  const sender = message.senderId || "Ẩn danh";
  const content = message.deletedAt ? "(Tin nhắn đã bị xoá)" : message.content;
  const editedText = message.editedAt ? " (đã chỉnh sửa)" : "";

  li.innerHTML = `
      <h4>${sender}${editedText}</h4>
      <p>${content ?? ""}</p>
      <time>${formatDate(message.createdAt)}</time>
    `;

  messageListEl.appendChild(li);
};

const upsertMessage = (message) => {
  if (!message) return;
  messages = [...messages, message];
  appendMessage(message);
};

const ensureDefaultRoom = async () => {
  setStatus("Đang tải phòng chat...");
  const response = await fetch(`${apiBase}/rooms`);
  if (!response.ok) {
    const message = await getErrorMessage(response, "Không thể tải danh sách phòng.");
    throw new Error(message);
  }
  const rooms = await response.json();
  if (rooms.length > 0) {
    return rooms[0];
  }

  setStatus("Đang tạo phòng chat chung...");
  const createResponse = await fetch(`${apiBase}/rooms`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ name: "Phòng chung" }),
  });

  if (!createResponse.ok) {
    const message = await getErrorMessage(createResponse, "Không thể tạo phòng chung.");
    throw new Error(message);
  }

  return createResponse.json();
};

const getWebSocketUrl = () => {
  if (window.location.protocol === "file:") {
    return "ws://localhost:8080/ws";
  }
  const protocol = window.location.protocol === "https:" ? "wss" : "ws";
  return `${protocol}://${window.location.host}/ws`;
};

const sendEnvelope = (type, payload) => {
  if (!socket || socket.readyState !== WebSocket.OPEN) {
    setStatus("WebSocket chưa sẵn sàng.", true);
    return;
  }
  const id = window.crypto?.randomUUID?.() ?? `${Date.now()}-${Math.random()}`;
  const envelope = {
    v: 1,
    type,
    id,
    ts: Date.now(),
    payload,
  };
  socket.send(JSON.stringify(envelope));
};

const subscribeToRoom = (roomId) => {
  sendEnvelope("SUBSCRIBE", {
    destination: `rooms/${roomId}`,
  });
};

const connectWebSocket = (roomId) => {
  if (socket) {
    socket.close();
  }
  socket = new WebSocket(getWebSocketUrl());
  socket.addEventListener("open", () => {
    setStatus("WebSocket đã kết nối.");
    subscribeToRoom(roomId);
  });
  socket.addEventListener("message", (event) => {
    try {
      const data = JSON.parse(event.data);
      if (data?.type === "MESSAGE" && data.payload?.roomId === roomId) {
        upsertMessage(data.payload);
      }
    } catch (error) {
      console.error(error);
    }
  });
  socket.addEventListener("close", () => {
    setStatus("WebSocket đã ngắt kết nối.", true);
  });
  socket.addEventListener("error", () => {
    setStatus("Không thể kết nối WebSocket.", true);
  });
};

const loadMessages = async (room) => {
  if (!room) return;
  refreshMessagesBtn.disabled = false;
  roomTitleEl.textContent = `Phòng chat - ${room.name}`;
  setStatus(`Đang tải tin nhắn phòng ${room.name}...`);
  try {
    const response = await fetch(`${apiBase}/rooms/${room.id}/messages?limit=50`);
    if (!response.ok) {
      const message = await getErrorMessage(response, "Không thể tải tin nhắn.");
      throw new Error(message);
    }
    messages = await response.json();
    renderMessages(messages);
    setStatus(`Đã tải ${messages.length} tin nhắn.`);
  } catch (error) {
    setStatus(error.message, true);
  }
};

sendMessageForm.addEventListener("submit", (event) => {
  event.preventDefault();
  const content = messageInput.value.trim();
  if (!content || !currentRoom) return;
  sendEnvelope("SEND_MESSAGE", {
    roomId: currentRoom.id,
    content,
  });
  messageInput.value = "";
});

refreshMessagesBtn.addEventListener("click", () => {
  loadMessages(currentRoom);
});

toggleSliderBtn.addEventListener("click", () => {
  isSliderMode = !isSliderMode;
  toggleSliderBtn.textContent = isSliderMode ? "Tắt trượt" : "Chế độ trượt";
  messageListEl.classList.toggle("message-list--slider", isSliderMode);
});

const boot = async () => {
  try {
    currentRoom = await ensureDefaultRoom();
    await loadMessages(currentRoom);
    connectWebSocket(currentRoom.id);
  } catch (error) {
    setStatus(error.message, true);
  }
};

boot();
