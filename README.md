# mobile
Android and React Native apps -  3rd year, first semester projects

The items fetched from the API represent gas station logs(id, car brand and type of fuel).

The API has a web socket configured on port 4000, and it broadcasts to all clients when addition, deletion or update is done.
There is a JWT Token generated at login, that is further required for authorization.

For offline usage React makes use of AsyncStorage and Android has a Room database.
Data is synchronized when server is up and running again.

