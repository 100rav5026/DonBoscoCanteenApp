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

router.post('/',(req,res)=>{
    var id = req.body.id;
    console.log(id)
      db.get('Select name,email,phone,image1 from profile where id =(?)',[id], function(err,row) {
        if(err)
        { 
          console.log(err);
          res.sendStatus(400);
        } 
        else
        console.log(row)
        res.send(row);
    });
    
});
module.exports=router;
