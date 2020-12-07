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

router.get('/',(req,res)=>{
    var id = req.body.id;
      
    db.get('Select image,name,email,phone from profile where id =(?)',[id], function(err,row) {
        if(err)
        { 
          console.log(err);
          res.sendStatus(400);
        } 
        else
        res.send(row);
    });
});
module.exports=router;
