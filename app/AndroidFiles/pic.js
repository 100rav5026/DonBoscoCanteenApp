var express = require('express');
var app = express();
var bodyParser = require('body-parser');
app.use(express.json());
app.use(bodyParser.json());
var router = express.Router();
app.use(bodyParser.urlencoded({
    extended: true
}));

const sqlite3 = require('sqlite3').verbose();
let db = new sqlite3.Database('./canteen.db', (err) => {
  if (err) {
    return console.error(err.message);
  }
});


router.post('/', (req, res, next) => {
    console.log("HHHHHHH");
    var file = req.body.file;
    var user_id = req.body.user_id;
    console.log(user_id);
    
    if (!file) {
      const error = new Error('No File')
      error.httpStatusCode = 400
      return next(error)
    }else{
        var temp = file.replace(/^data:image\/png;base64,/,"");
        var locationImg="uploads/"+user_id+"Profile.png";
        var imgLoc = locationImg;
        console.log(imgLoc);
        try{
            require("fs").unlinkSync(locationImg);
        }catch(err){
            console.log(err);
        }
        require("fs").writeFile(locationImg,temp,'base64',function(err){
            console.log(err);
        });
        db.run("Update profile set image=(?) where id=(?)",[imgLoc, user_id], function (err, result) {
        if(err)
        { console.log(err);
          res.status(400).send({ "error": "Username or Password is incorrect" });
        } 
        else
        res.send({"status":true,"remark":"upload successfully"});
    });
      
    }
    
})
module.exports=router;
