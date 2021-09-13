const express = require('express');
const router = express.Router();

const albumController = require('../Controller/AlbumController')

router.get('/',albumController.getAlbum)

module.exports = router;
