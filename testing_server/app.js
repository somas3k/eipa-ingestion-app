const express = require("express");
const app = express();

app.listen(3000, () => {
    console.log("Server running on port 3000");
});
app.use(express.json({limit: '50mb'}));
app.use(express.urlencoded({limit: '50mb'}));

app.post('/events', (req, res, next) => {
    if (req.body.length > 0 && req.body.length < 10) {
        console.log(req.body);
    } else {
        console.log(req.body.length)
    }
    res.json('Posting events ends well').ok;
})
