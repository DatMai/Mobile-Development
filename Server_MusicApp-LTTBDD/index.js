require('dotenv').config()
const express = require('express')
const app = express()

const admin = require('firebase-admin')
const serviceAccount = require('./serviceAccountKey.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://humanresourcemanager-57f05.firebaseio.com"
  });

const bodyParser = require('body-parser')
const cors = require('cors')
const server = require('http').createServer(app)

const port = process.env.PORT || 8000

app.use(cors({origin: true}))
app.use(bodyParser.json())

app.get('/',(req, res)=>{
    res.send('alo')
})

const albumRoute = require('./Routes/albumRoute');
const favoriteRoute = require('./Routes/favoritesRoute');
const kindRoute = require('./Routes/kindRoute');
const playlistRoute = require('./Routes/playlistRoute');
const songsRoute = require('./Routes/songsRoute');
const userRoute = require('./Routes/userRoute');

app.use('/album', albumRoute);
app.use('/favorite', favoriteRoute);
app.use('/kind', kindRoute);
app.use('/playlist', playlistRoute);
app.use('/songs', songsRoute);
app.use('/user', userRoute);

server.listen(port, ()=>{
    console.log('Server is running on port: ' + port)
})