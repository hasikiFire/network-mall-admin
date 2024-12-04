const net = require("net");

const port = 9000;

const server = net.createServer((socket) => {
  console.log("New connection established.");

  socket.on("data", (data) => {
    console.log(`Received data: ${data}`);
    // 在这里处理接收到的数据
    socket.write("OK");
  });

  socket.on("end", () => {
    console.log("Connection ended.");
  });

  socket.on("error", (err) => {
    console.error(`Connection error: ${err.message}`);
  });
});

server.listen(port, "127.0.0.1", () => {
  console.log(`TCP server is running on tcp://127.0.0.1:${port}`);
});
