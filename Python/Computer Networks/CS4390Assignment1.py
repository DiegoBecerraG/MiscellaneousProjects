from socket import *

if __name__ == "__main__":
    serverPort = 8080
    serverSocket = socket(AF_INET, SOCK_STREAM)
    serverSocket.bind(('localhost', serverPort))
    serverSocket.listen(1)
    print('The server is ready to receive')

    while 1:
        connectionSocket, addr = serverSocket.accept()
        HTTPrequest = connectionSocket.recv(1024).decode()

        try:
            specificFile = HTTPrequest.split()[1][1:]  # Gets rid of '/'
            if specificFile == '':
                specificFile = 'index.html'

            f = open(specificFile, 'r')
            requestedFile = f.read()
            f.close()
            header = 'HTTP/1.1 200 OK\r\n\n'
        except FileNotFoundError:
            header = 'HTTP/1.1 404 Not Found\r\n\n'
            requestedFile = '<html><head><title>Error</title></head><body><p>I am an Error text</p></body></html>'

        response = header + requestedFile
        connectionSocket.send(response.encode())
        connectionSocket.close()
