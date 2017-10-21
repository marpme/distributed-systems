# Übung No.1 

## 1. Zeitserver
### Finden Sie einen öffentlich zugänglichen Zeitserver, und implementieren Sie mit Hilfe einer Java NTP Library eine Anwendung, welche das aktuelle Datum und Uhrzeit auf der Konsole ausgibt.
    Das Programm benutzt eine NTP library von der Apachi Commons Net API v3.6

  **a. Was ist UTC und wo erfolgt die Anpassung in die aktuelle Zeitzone?**
  
    UTC ist die Coordinated Universal Time - also die Weltzeit. Die Anpassung erfolgt lokal.
    
  **b. Was ist NTP und welcher Port wird verwendet?**
  
    NTP ist das Network Time Protocol, welches den Port 123 (UDP) zur Kommunikation verwendet.
    
  **c. Was ist das offset?**
  
    Der Offset ist die Hälfte der Differenz der Laufzeiten der Pakete Client-Server und Server-Client.
    Es wird also ermittelt, wie sehr sich die Verbindung Client->Server zu Server->Client unterscheidet, um präzise Zeitangaben zu ermöglichen.
    Die Rechenzeit wird abgezogen nur die Laufzeit der Pakete im Netzwerk zählt.
        Berechnung: ((S1 - C1) - (C2 - S2)) / 2    [S = server, C = client]
	
  **d. Was ist das root delay?**
  
    Das root delay ist die totale Rundlaufzeit des Paketes im Netzwerk ohne Rechenlaufzeit.
        Berechnung: Der Delay ist: ((C2 - C1) - (S2 - S1))    [S = server, C = client]
	
  **e. Was ist ein leap?**
  
    Ein leap ist ein angekündigter Sprung in der Zeit - eine leap second die zur Korrektur der Zeit am Tagesende angewendet wird.
    
  **f. Wozu wird NTP im Kontext verteilte Systeme benötigt?**
  
    NTP wird benötigt um einen gemeinsamen Zugriff auf Ressourcen bei Verteilten Systemen zu koordinieren und um festzustellen, welcher Prozess ein Ereignis zuerst ausgelöst hast.

---

## 2. DNS Records
#### Aufgabe: Unter der Benutzung der UNIX Commands (nslookup, dig) herausfinden, welche Bedeutung die einzelnen Einträge in einem DNS Server haben.

  **a. Was ein A-Record?**
  
	ein A Record ist ein Eintrag für eine bestimmte IP (bspw. 172.129.21.9)
  
  **b. Was ist ein PTR Ressource Record?**
 
  	ist eine Record zum zuweisen von einen oder mehreren Domainnamen zu einer IP Adresse
  
  **c. Was ist ein MX Record?**
  
  	ein Eintrag zum denfinieren eines Mail-Server (MailExchange)
  
  **d. Was sind DNS SRV und wozu können diese genutzt werden?**
  
  	SRV's sind die einzelnen Einträge die später als Records hinterlegt werden
     	Werden zur Anlage einzelner Dienst genutzt wie zum Beipsiel um eine IP Adresse zu hinterlegen
     
  
 **e. Welche Alternativen gibt es zu DNS, um IP-Namen aufzulösen?**
 
 	Einen weiteren Nameservice den man nutzen kann lautet: `NetBIOS` und arbeitet auf dem UDP Port 137

---

## 3. Interfaces
#### Aufgabe: Das verstehen von einzelnen Netzwerk Interfaces und die bedeutung der einzelnen Komponenten wie IP Adressen und Netzwerkmasken
  a.
  
```sh
~/Desktop/distributed-systems on  master ⌚ 16:37:47
$ ifconfig
lo0: flags=8049<UP,LOOPBACK,RUNNING,MULTICAST> mtu 16384
	options=1203<RXCSUM,TXCSUM,TXSTATUS,SW_TIMESTAMP>
	inet 127.0.0.1 netmask 0xff000000
	inet6 ::1 prefixlen 128
	inet6 fe80::1%lo0 prefixlen 64 scopeid 0x1
	nd6 options=201<PERFORMNUD,DAD>
en0: flags=8863<UP,BROADCAST,SMART,RUNNING,SIMPLEX,MULTICAST> mtu 1500
	ether f4:0f:24:19:42:e7
	inet6 fe80::8b9:cfdf:c7bc:a036%en0 prefixlen 64 secured scopeid 0x6
	inet 141.45.209.46 netmask 0xfffff800 broadcast 141.45.215.255
	nd6 options=201<PERFORMNUD,DAD>
	media: autoselect
	status: active
awdl0: flags=8943<UP,BROADCAST,RUNNING,PROMISC,SIMPLEX,MULTICAST> mtu 1484
	ether 42:9e:82:2c:94:9a
	inet6 fe80::409e:82ff:fe2c:949a%awdl0 prefixlen 64 scopeid 0x8
	nd6 options=201<PERFORMNUD,DAD>
	media: autoselect
	status: active
```

  **a. Welche Netzwerke werden durch die einzelnen Interfaces beschrieben?**

  	1. **lo0:** das Localhost interface, welcher den Zugriff per 127.0.0.1 ermöglicht
  	2. **en0:** die aktuelle Netzwerkverbindung über mein Wi-Fi (mit IPv4 und ein IPv6 Adresse)
  	3. **awdl0:** ist ein Apple Wireless Direct Link interface zur direkten Verbindung mit anderen Apple Geräten

**b. Welchen Sinn macht es, unterschiedliche Subnet-Masken zu benutzen?**

	Zur Schachtelung eines gesamten Netzwerkes in einzelne Sub-Netzwerke um es beispielsweise in Abteilungen aufteilen zu können.

**c. Geben Sie zu einem Interface alle IP-Adresse an, an welche direkt (ohne Routing) ausgeliefert
werden können. Tipp: stellen Sie die Adressen und die Netzwerk-Maske in Binärschreibweise
dar.**

	
	Address:   141.45.208.0          10001101.00101101.11010 000.00000000
	Netmask:   255.255.248.0 = 21    11111111.11111111.11111 000.00000000

	Broadcast: 141.45.215.255        10001101.00101101.11010 111.11111111
	Min Host:   141.45.208.1         10001101.00101101.11010 000.00000001
	Max Host:   141.45.215.254       10001101.00101101.11010 111.11111110
	
	Alle IP Adressen im bereich von 141.45.208.1 bis 141.45.215.254 sind ohne Routing direkt ansprechbar da sie in meinem Subnetz sind.

---

## 4. Routing
#### Aufgabe: Mittels `traceroute` erklären zu können was Default Gateways sind und welche einzelnen Schritte notwendig sind um Information über den Verwendeten Router zu bekommen.

   **a. Was versteht man unter einem Default Gateway?**
   
 	Ein default Gateway stellt die direkte Verbindung zum Router da um Anfragen an außerhalb des eigenen Subnetzes zu senden.
   
   **b. Wie arbeitet das Kommando traceroute, um Informationen über die verwendeten Router zu
bekommen?** 

	`traceroute` sendet zu erst ein Packet mit dem TTL 1 and das Default Gateway um die Informationen über den ersten Router zu bekommen un dann mit TTL 2 für den zweiten Router. Immer so weiter bis er keine ICMP Time Exceeding Message mehr bekommt. Dadurch kann er sicherstellen, das das Paket am letzten Server angekommen ist.
