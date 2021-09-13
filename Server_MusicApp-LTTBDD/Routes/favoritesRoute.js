const express = require('express')
const router = express.Router()

const favoritesController = require('../Controller/FavoritesController')

router.post('/addItem', favoritesController.addNew);
router.put('/removeItem', favoritesController.remove);

module.exports = router