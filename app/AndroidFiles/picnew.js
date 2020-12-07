var express = require('express');
var app = express();
var bodyParser = require('body-parser');
app.use(express.json());
var router = express.Router();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));
var sqlite3 = require('sqlite3');
var fs = require('fs');

var db = new sqlite3.Database('./canteen.db'); //Database path
// db.run("CREATE TABLE Bdata (image BLOB)",function(err)    
 
router.get('/',(req,res)=>{
   var n = req.body.fname;
   var d= req.body.direct;
   var id = req.body.id;
   var name = req.body.name;
   var email = req.body.email;
   var phone = req.body.phone;

   var bData = fs.readFileSync(d+"/"+n);
   db.run('Update profile set image=(?),name = (?),email=(?),phone=(?) where id =(?)',[bData,name,email,phone,id], function(err,row)    
   {
      if (err) 
      console.log(err);
      res.sendStatus(200);    
   });

   });
module.exports=router;