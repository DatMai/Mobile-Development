const express = require('express');
const router = express.Router();

const userController = require('../Controller/UserController');

router.post('/signup', userController.Signup);
router.put('/login', userController.Login);
router.put('/changePassword', userController.changePassword);
router.put('/changeStatus', userController.changeStatusLink);
router.get('/getInfo', userController.getUserInfo);

module.exports = router


