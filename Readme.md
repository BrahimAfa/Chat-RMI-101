## STEP 1 : clone this repo : 
 ```shell
$ git clone https://github.com/BrahimAfa/Chat-RMI-101.git
```
## STEP 2 : change Directory to Repository :

enter to the cloned Repository and then to src folder

````shell
$ cd Chat-RMI-101/src
````

## STEP 3 : Compile Client and Server JAVA Files
Client : 
```shell
$  javac com/ensas/client/*.java 
```
Server :
```shell
$ javac com/ensas/server/*.java 
```

## STEP 4 : run Server and Client:

#### Server :
```shell
$ java com.ensas.server.ChatServer 
```
***NOTE: AFTER RUNNING SERVER OPEN NEW TERMINAL AND RUN THE FOLLOWING COMMAND FOR CLIENT UI IN THE _SAME PATH_**

#### Client : 
```shell
$ java com.ensas.client.ClientUI 
```

and to run Multiple clients just run the previous command multiple times  