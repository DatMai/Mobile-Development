const express = require('express');
const router = express.Router();

const SongsController = require('../Controller/SongsController');

router.get('/rank', SongsController.getRank)
router.get('/suggest', SongsController.suggestSong)
router.put('/search', SongsController.searchMusic)
router.put('/getSongsFromList', SongsController.getSongFromList);

module.exports = router


