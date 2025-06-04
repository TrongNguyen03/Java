'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var fileInput = document.querySelector('#fileInput');
var fileStatus = document.getElementById('fileStatus');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
  '#2196F3', '#32c787', '#00BCD4', '#ff5652',
  '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
  username = document.querySelector('#name').value.trim();

  if(username) {
      usernamePage.classList.add('hidden');
      chatPage.classList.remove('hidden');

      var socket = new SockJS('/ws');
      stompClient = Stomp.over(socket);

      stompClient.connect({}, onConnected, onError);
  }
  event.preventDefault();
}

function onConnected() {
  stompClient.subscribe('/topic/public', onMessageReceived);

  stompClient.send("/app/chat.addUser",
      {},
      JSON.stringify({sender: username, type: 'JOIN'})
  )

  connectingElement.classList.add('hidden');
}

function onError(error) {
  connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
  connectingElement.style.color = 'red';
}

function sendMessage(event) {
  event.preventDefault();

  // Nếu có file được chọn thì gửi file
  if(fileInput.files.length > 0 && stompClient) {
      var file = fileInput.files[0];
      var reader = new FileReader();
      reader.onload = function(evt) {
          var chatMessage = {
              sender: username,
              type: 'FILE',
              fileName: file.name,
              fileType: file.type,
              fileContent: evt.target.result
          };
          stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
          fileInput.value = "";
          fileStatus.textContent = "";
      };
      reader.readAsDataURL(file);
      // Không gửi text trong lần này
      messageInput.value = '';
      return;
  }

  var messageContent = messageInput.value.trim();
  if(messageContent && stompClient) {
      var chatMessage = {
          sender: username,
          content: messageInput.value,
          type: 'CHAT'
      };
      stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
      messageInput.value = '';
  }
}

function onMessageReceived(payload) {
  var message = JSON.parse(payload.body);

  var messageElement = document.createElement('li');

  if(message.type === 'JOIN') {
      messageElement.classList.add('event-message');
      message.content = message.sender + ' join room';
      var textElement = document.createElement('p');
      textElement.textContent = message.content;
      messageElement.appendChild(textElement);
  } else if (message.type === 'LEAVE') {
      messageElement.classList.add('event-message');
      message.content = message.sender + ' out room';
      var textElement = document.createElement('p');
      textElement.textContent = message.content;
      messageElement.appendChild(textElement);
  } else if (message.type === 'FILE') {
      messageElement.classList.add('chat-message');

      var avatarElement = document.createElement('i');
      var avatarText = document.createTextNode(message.sender[0]);
      avatarElement.appendChild(avatarText);
      avatarElement.style['background-color'] = getAvatarColor(message.sender);
      messageElement.appendChild(avatarElement);

      var usernameElement = document.createElement('span');
      var usernameText = document.createTextNode(message.sender);
      usernameElement.appendChild(usernameText);
      messageElement.appendChild(usernameElement);

      // Phân biệt file ảnh và file thường
      if(message.fileType && message.fileType.startsWith("image/")) {
          var img = document.createElement('img');
          img.src = message.fileContent;
          img.alt = message.fileName;
          img.style.maxWidth = "200px";
          img.style.display = "block";
          messageElement.appendChild(img);
          // Hiện tên file dưới ảnh
          var fname = document.createElement('small');
          fname.textContent = message.fileName;
          messageElement.appendChild(fname);
      } else {
          var fileLink = document.createElement('a');
          fileLink.href = message.fileContent;
          fileLink.textContent = message.fileName || "Download file";
          fileLink.download = message.fileName;
          messageElement.appendChild(fileLink);
      }
  } else {
      messageElement.classList.add('chat-message');

      var avatarElement = document.createElement('i');
      var avatarText = document.createTextNode(message.sender[0]);
      avatarElement.appendChild(avatarText);
      avatarElement.style['background-color'] = getAvatarColor(message.sender);

      messageElement.appendChild(avatarElement);

      var usernameElement = document.createElement('span');
      var usernameText = document.createTextNode(message.sender);
      usernameElement.appendChild(usernameText);
      messageElement.appendChild(usernameElement);

      var textElement = document.createElement('p');
      var messageText = document.createTextNode(message.content);
      textElement.appendChild(messageText);
      messageElement.appendChild(textElement);
  }

  messageArea.appendChild(messageElement);
  messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
  var hash = 0;
  for (var i = 0; i < messageSender.length; i++) {
      hash = 31 * hash + messageSender.charCodeAt(i);
  }
  var index = Math.abs(hash % colors.length);
  return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)

// Xử lý hiện trạng thái file đã chọn
fileInput.addEventListener('change', function() {
    if(fileInput.files && fileInput.files.length > 0) {
        const file = fileInput.files[0];
        fileStatus.innerHTML = `
            join: <b>${file.name}</b>
            <button type="button" class="remove-file" title="RemoveFile">&times;</button>
        `;
        // Xóa file khi click nút x
        fileStatus.querySelector('.remove-file').onclick = function() {
            fileInput.value = "";
            fileStatus.textContent = "";
        };
    } else {
        fileStatus.textContent = '';
    }
});
// Lightbox logic
document.addEventListener('DOMContentLoaded', function() {
    const lightbox = document.getElementById('imgLightbox');
    const lightboxImg = document.getElementById('imgLightboxImg');
    const lightboxClose = document.getElementById('imgLightboxClose');

    // Đóng khi bấm vào nút X hoặc nền đen
    lightboxClose.onclick = function() { lightbox.style.display = 'none'; }
    lightbox.onclick = function(e) {
        if (e.target === lightbox) lightbox.style.display = 'none';
    };

    // Khi có ảnh chat, bấm vào thì hiện lightbox
    document.body.addEventListener('click', function(e){
        if(e.target.tagName === 'IMG' && e.target.closest('.chat-message')) {
            lightboxImg.src = e.target.src;
            lightboxImg.alt = e.target.alt || '';
            lightbox.style.display = 'flex';
        }
    });
});