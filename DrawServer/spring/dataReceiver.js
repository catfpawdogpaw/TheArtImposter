// dataReceiver.js
const express = require('express');
const router = express.Router();

router.post('/receive-data', (req, res) => {
    const data = req.body;
    console.log("Received data:", data);
    res.send('Data received');
});

module.exports = router;
