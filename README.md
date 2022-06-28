# ing-sw-2022-Shpakov-Stefanizzi-Tischer

* [The Game](#game)
* [Implemented Functionalities](#functionalities)
* [Installation](#installation)
* [Server](#server)
* [Client](#client)
* [How to play](#howtoplay)
* [Tools](#tools)
* [Test and Docs](#test)

 <img src="https://m.media-amazon.com/images/I/71mAjzs5xwL._AC_SL1500_.jpg" width=300px height=300px align="right" />


## The Game <a name="game"></a>
This repo contains a **Java** version of the game [*Eriantys*](https://www.craniocreations.it/prodotto/eriantys/). The project has been developed for the course **Software Engineering** at Politecnico di Milano as a final part of examination for the Bachelor in Computer Engineering. The contributors were:
* [Mykhailo Shpakov](https://github.com/MykhailoShpakovPoliMi)
* [Giacomo Stefanizzi](https://github.com/jackstefa)
* [Alessandro Tischer](https://github.com/AlexTischer)

## Implemented Funcionalities <a name="functionalities"></a>
| Functionalities    |                      Status                       |
|:-------------------|:-------------------------------------------------:|
| Basic Rules        |                        🟩                         |
| Complete Rules     |                        🟩                         |
| Socket             |                        🟩                         |
| CLI                |                        🟩                         |
| GUI                |                        🟨                         |
| Character Cards    |                        🟩                         |
| Four Players Match |                        🟩                         |
| Multiple Matches   |                        🟥                         |
| Persistence        |                        🟥                         |
| Resilience         |                        🟨                         |

#### Legend
🟥Not Implemented &nbsp;&nbsp;&nbsp;&nbsp;
🟨Implementing&nbsp;&nbsp;&nbsp;&nbsp; 
🟩Implemented

## Installation <a name="installationENG"></a>
To start the game you need to download [JavaSE 15](https://www.oracle.com/it/java/technologies/javase-downloads.html) (or updated versions).

Download the repo as it follow
```bash
git clone https://github.com/AlexTischer/ing-sw-2022-Shpakov-Stefanizzi-Tischer.git
```
on linux. For Windows should be necessary download the repo in zip format and then unzip it.
The executable files are stored in the `/shade` directory.

## How to start the server <a name="serverENG"></a>
Open the **ngrok** directory with the terminal (linux or windows) and open a TCP connection at port `12345`, as it follow
```bash
./ngrok tcp 12345
```
The port number has to be `12345`. If it should be necessary to modify the port, open the project class `Server` and edit the attribute, then rebuild the *.jar* file.

Open the terminal at dir `\shade` and copy
```bash
java -jar SERVERG_C39.jar
```
Now the server has started and can be leaved powered on even a match ends.

## How to start the client <a name="clientENG"></a>
To connect the client will be necessary open the terminal at the `\shade` dir and copy
```bash
java -jar CLIENT_C39.jar ADDRESS PORT
```
where `ADDRESS` and `PORT` are the server address and port. In our case, the **ngrok** app will print a text as it follow
```bash
Session Status: online
Account: YOURACCOUNT (Plan: Free)
Version: 2.3.40
Region: United States (us)
Web Interface: http://127.0.0.1:4040
Forwarding:  tcp://4.tcp.ngrok.io:11387 -> localhost:12345                                              
```
**4.tcp.ngrok.io** will be the address and **11387** the port.

On default the client will open the GUI, but if you add the parameter `cli` at the end of the command it will be possible start the game from command-line.
```bash
java -jar CLIENT_C39.jar ADDRESS PORT cli
```

## How to play<a name="howtoplayENG"></a>
A game can be started in three different ways: I can create a room where my friends can join me (a room can be joined through its name); I can join an online public multiplayer filling the infos with my personal nickname and with how many players I want play with; I can play alone offline.

You can find the english rules [here](https://github.com/GiorgioSeguini/ing-sw-2021-Seguini-Villa-Zeni/blob/master/Documentation/Masters-of-Renaissance_small.pdf)

Ps. It is alway possible rejoin an alredy started match if I have my `nickname` and the `room` name.

## Tools <a name="toolsENG"></a>
In this project were used the following tools:
* [LucidChart](https://lucid.app/) - UML Diagram
* [Maven](https://maven.apache.org/) - Dependency Management
* [IntelliJ](https://www.jetbrains.com/idea/) - IDE
* [JavaFX](https://openjfx.io) - Graphical Framework


## Test and Documentation <a name="testENG"></a>
You can find the documentation [here](https://github.com/GiorgioSeguini/ing-sw-2021-Seguini-Villa-Zeni/tree/master/Documentation).

The tests have reached the following coverage.


![Test result](https://github.com/GiorgioSeguini/ing-sw-2021-Seguini-Villa-Zeni/blob/master/Documentation/total.png)
![Test result](https://github.com/GiorgioSeguini/ing-sw-2021-Seguini-Villa-Zeni/blob/master/Documentation/server.png)
![Test result](https://github.com/GiorgioSeguini/ing-sw-2021-Seguini-Villa-Zeni/blob/master/Documentation/costant.png)
![Test result](https://github.com/GiorgioSeguini/ing-sw-2021-Seguini-Villa-Zeni/blob/master/Documentation/client.png)