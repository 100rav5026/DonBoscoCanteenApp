var express = require('express');
var app = express();
var bodyParser = require('body-parser');
//var crypto = require('crypto');
app.use(express.json());
app.use(bodyParser.urlencoded({
    extended: true
}));
app.use(bodyParser.json());

const sqlite3 = require('sqlite3').verbose();
// open database in memory
let db = new sqlite3.Database('./canteen.db', (err) => {
  if (err) {
    return console.error(err.message);
  }
  console.log('Connected to Canteen database.');
});
app.post('/',(req,res)=>{
    //res.sendStatus(200);
    var username = req.body.username;
    console.log(req.body.username);
    var password = req.body.password;
    console.log(password);
    
  //function hashPassword(password) {
  //var hash = crypto.createHash('sha256');
  //hash.update(password);
  //return hash.digest('hex');
//}
    //h=hashPassword(password);
    db.all('Select * from users where username=?',[username], function(err,row){
		console.log(row)
		if(row.length==0){
			res.sendStatus(400);
		}
		else{
			row.forEach(element =>{ 
			if(element.password != password)
			{
				res.sendStatus(400);
			}
			else{
			    res.sendStatus(200);
		}})}
    //if (err) {
      //  return console.log(err.message);
    //}
  });
});
app.listen(8080, (req, res) => {
    console.log("Listening on 8080");
  });
  
