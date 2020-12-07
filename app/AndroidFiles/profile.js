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
    var name = req.body.name;
    var email = req.body.email;
    var phone = req.body.phone;
    var array = req.body.array;
    console.log(id)
    console.log(name)
    console.log(email)
    console.log(phone)

      db.run('Update profile set name = (?),email=(?),phone=(?), image1=(?) where id =(?)',[name,email,phone,array,id], function(err,row) {
        if(err)
        { console.log(err);
          res.sendStatus(400);
        } 
        else
        res.send({name,email,phone,array});
    });
    
});
module.exports=router;
