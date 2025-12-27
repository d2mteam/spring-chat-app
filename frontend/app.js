const apiBase = "/api";

const statusEl = document.getElementById("status");
const roomListEl = document.getElementById("roomList");
const roomTitleEl = document.getElementById("roomTitle");
const messageListEl = document.getElementById("messageList");
const refreshRoomsBtn = document.getElementById("refreshRooms");
const refreshMessagesBtn = document.getElementById("refreshMessages");
const createRoomForm = document.getElementById("createRoomForm");
const roomNameInput = document.getElementById("roomName");

let currentRoomId = null;
let rooms = [];

const setStatus = (message, isError = false) => {
  statusEl.textContent = message;
  statusEl.style.background = isError ? "#fee2e2" : "#e2e8f0";
  statusEl.style.color = isError ? "#991b1b" : "#0f172a";
};

const formatDate = (value) => {
  if (!value) return "";
  const date = new Date(value);
  return new Intl.DateTimeFormat("vi-VN", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(date);
};

const renderRooms = () => {
  roomListEl.innerHTML = "";
  if (rooms.length === 0) {
    roomListEl.innerHTML = "<li>Chưa có phòng nào. Hãy tạo phòng mới.</li>";
    return;
  }

  rooms.forEach((room) => {
    const li = document.createElement("li");
    li.textContent = room.name;
    li.dataset.roomId = room.id;
    if (room.id === currentRoomId) {
      li.classList.add("active");
    }
    li.addEventListener("click", () => {
      currentRoomId = room.id;
      renderRooms();
      loadMessages(room);
    });
    roomListEl.appendChild(li);
  });
};

const renderMessages = (messages) => {
  messageListEl.innerHTML = "";
  if (messages.length === 0) {
    messageListEl.innerHTML = "<li>Phòng này chưa có tin nhắn.</li>";
    return;
  }

  messages.forEach((message) => {
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
  });
};

const loadRooms = async () => {
  setStatus("Đang tải phòng chat...");
  try {
    const response = await fetch(`${apiBase}/rooms`);
    if (!response.ok) {
      throw new Error("Không thể tải danh sách phòng.");
    }
    rooms = await response.json();
    renderRooms();
    setStatus("Đã tải danh sách phòng.");
  } catch (error) {
    setStatus(error.message, true);
  }
};

const loadMessages = async (room) => {
  if (!room) return;
  refreshMessagesBtn.disabled = false;
  roomTitleEl.textContent = `Tin nhắn - ${room.name}`;
  setStatus(`Đang tải tin nhắn phòng ${room.name}...`);
  try {
    const response = await fetch(`${apiBase}/rooms/${room.id}/messages?limit=50`);
    if (!response.ok) {
      throw new Error("Không thể tải tin nhắn.");
    }
    const messages = await response.json();
    renderMessages(messages);
    setStatus(`Đã tải ${messages.length} tin nhắn.`);
  } catch (error) {
    setStatus(error.message, true);
  }
};

createRoomForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const name = roomNameInput.value.trim();
  if (!name) return;

  setStatus("Đang tạo phòng...");
  try {
    const response = await fetch(`${apiBase}/rooms`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name }),
    });

    if (!response.ok) {
      throw new Error("Không thể tạo phòng.");
    }

    roomNameInput.value = "";
    await loadRooms();
    const newRoom = rooms.find((room) => room.name === name);
    if (newRoom) {
      currentRoomId = newRoom.id;
      renderRooms();
      await loadMessages(newRoom);
    }
    setStatus("Tạo phòng thành công.");
  } catch (error) {
    setStatus(error.message, true);
  }
});

refreshRoomsBtn.addEventListener("click", () => {
  loadRooms();
});

refreshMessagesBtn.addEventListener("click", () => {
  const room = rooms.find((item) => item.id === currentRoomId);
  loadMessages(room);
});

loadRooms();
