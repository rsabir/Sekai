# Sekai

Sekai is a distributed geolocalisation system base on OSM. It's compatible with huge traffic and contains graphic interface to administrate each server.
You will find in this git all the sources of the server Sekai. There's two versions of Sekai :

 * OSM-Mysql : stable version which use Mysql
 * OSM : deprecated one, use SQLite

You can find the sources and wars (bin) Sekai Client and server in google drive:
 https://drive.google.com/folderview?id=0BywXTC21h2yVa0M3UTJoUnpFZHM&usp=sharing
 
 The link to the client's github repository:
 https://github.com/ZakariaHili/Sekai_Client

***The sekai servers are open source and are all liscenced in MIT Liscence, which means you can use it freely and without restriction. We will be very glad to receive your contribution.***

## Installation

To use Sekai, you can either install it manually or use the virtual machine (ubuntu server) present in the drive. For installation, you have to get **mysql**, a **jre** ( version 7) or jdk and **Tomcat 7**:

```
# apt-get install mysql-server
# apt-get install default-jre
# apt-get install tomcat7

```
After that, you need to get sekai.war in the drive or generate it with the sources present in the git.

Then you have to run the install.sh with ***root***
```
# chmod 755 install.sh
# bash install.sh
```

## How to use Sekai
### Configuration
First you have to start mysql then tomcat. After that, you have to acces the server whith his ip (private one or the public one) so he can know its ip. So as an example, you have to put : http://172.20.1.1:8080/GetMap. This url */GetMap* is used to see the map and the clients available in the server. Then you have you configure the config.json by accessing the url */SetConfig*. This json represent each server and the domain (area) that it control. The json have to be in this format:
```javascript
{"servers":
	[
         {
	    "host":"172.20.0.43",
	    "zone":{
		"maxlat":44.8448769,
		"minlat":44.8305549,
		"maxlon":-0.5275520000000142,
		"minlon":-0.6563579999999547
	    }
	 },{"host": "172.20.0.44"
	    [...]
```

Then you can make general settings, like which server will be asked for the *config.json*, the configuration of the **mysql**, by accessing the url */Settings*.

You can also make all this configurations by changing **config.json** and **setting.json** in */opt/sekai/*.

### Test
To test the server you have to send queries in */SendGPS* with this format:
```javascript
{
	"client":{
		"ID" : "value",
		"Position" : {	
			   "lat" : x,
			   "lon" : y
	        }
	},"isServer" : false
}
```
Here's a small command that will do the job:
```
curl -H "Content-Type: application/json" -X POST -d '{ "client": {"ID" : "MAC_ADRESS","Position" : {"lat" :44.8345549 ,"lon" : -0.53 }}"isServer" : false}' http://172.20.0.43:8080/SendGPS

```

You can also test with our clients that you can find in the git :
>https://github.com/ZakariaHili/JOSM/

### More Details
For more detail, please read our doc under the document folder or send us a mail at sabiretude At gmail.com

### TODO:
* Allow a crentralized authentication
* Moving from mysql to Kundera (Allowing multiple databases)
* Improve the security of communication with servers
* Changing the logs by using log4j
