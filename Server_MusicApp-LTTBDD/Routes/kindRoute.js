const express = require('express');
const router = express.Router();

const kindController = require('../Controller/KindController');

router.get('/', kindController.getFirst)
router.get('/next', kindController.getNext)

module.exports  = router;