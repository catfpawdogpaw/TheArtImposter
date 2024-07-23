const express = require("express");
const router = express.Router();

const PostController = require("../controller/boardController.js");
const postController = new PostController();

router.get("/3", postController.getTest3);

router.get("/1", postController.getTest1);

router.get("/2", postController.getTest2);

module.exports = router;
