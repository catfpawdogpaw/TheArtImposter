class BoardController {
    getTest1 = (req, res) => {
        res.json({ test: "gettest1" });
    };
    getTest2 = (req, res) => {
        res.send("gettest2");
    };
    getTest3 = (req, res) => {
        res.send("gettest3");
    };
}

module.exports = BoardController;
