const express = require('express');
const router = express.Router();

const playlistController = require('../Controller/PlaylistController');

router.post('/createNew', playlistController.createNew);
router.put ('/rename', playlistController.renamePlaylist);
router.put('/delete', playlistController.deletePlaylist);
router.get('/getUserPlaylists', playlistController.getUserPlaylists);
router.post('/addItem', playlistController.addItem);
router.put('/removeItem', playlistController.removeItem);

module.exports = router;