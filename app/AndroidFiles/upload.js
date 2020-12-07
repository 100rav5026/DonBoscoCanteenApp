var express = require('express');
var app = express();
var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

var multer = require('multer');

//Destination Of Images and Renaming Of Images
var storage = multer.diskStorage({
	destination: function(req,file,cb){
		cb(null,'uploads/');
	},
	filename: function(req,file,cb){
		cb(null,file.originalname);
	}
	
});
//Restriction On Image Size
var upload = multer({
	
	storage:storage
}).array('file',10);

//POST Request To Upload Image
app.post('/pic', (req,res) =>{
   	upload(req,res,function(err){
		//console.log(req);
		if(err){
			console.log(err);
			res.sendStatus(400);
		}
		else{	
			console.log("Succesfully Uploaded");
			res.sendStatus(200);
		}
	});
});


//Port Listenings
app.listen(8080, (req, res) => {
    console.log("Listening on 8080");
});
