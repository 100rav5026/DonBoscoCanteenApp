var express = require('express');
var app = express();
var bodyParser = require('body-parser');
app.use(express.json());
var router = express.Router();
app.use(bodyParser.json());
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
    var item_name = req.body.item_name;                                                                                                                                                                                                               
    var price = req.body.price;

    db.get('Delete from menu where item_name=(?)', [item_name], function(err,row) {
      if(err)
      {
        res.sendStatus(400);
      }
      else
      res.sendStatus(200);
  });
});

  module.exports=router;                                                                                            