var express = require('express');
var app = express();
var bodyParser = require('body-parser');
//var crypto = require('crypto');
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
    var password = req.body.password;
    console.log(password)
    
  //function hashPassword(password) {
  //var hash = crypto.createHash('sha256');
  //hash.update(password);
  //return hash.digest('hex');
//}
    //h=hashPassword(password);
   // JSONObject row = new JSONObject();
   db.get('Select id from users where id=(?)', [id], function(err,rows) {
    console.log(rows)
    if(!rows)
    {
      console.log("ok1");
      res.sendStatus(400);
    }
    else
    {
      db.get('Select * from users where id=(?)', [id], function(err,row) {
        console.log(row);
        if(row.password != password )
        {
          console.log("ok2");
          res.sendStatus(400);
        }
        else
        {console.log("ok2");  
                res.sendStatus(200);}
    });
    }
});
 
});

module.exports=router;
