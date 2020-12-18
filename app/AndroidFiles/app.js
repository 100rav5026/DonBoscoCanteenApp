var express = require('express');
var app = express();
var router = express.Router();
var bodyParser = require('body-parser');
app.use(express.json());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));


var login = require('./login');
app.use('/login',login);

//var view = require('./view');
//app.use('/view',view);

var pic = require('./pic');
app.use('/pic',pic);

var profile = require('./profile');
app.use('/profile',profile);

var disp_profile = require('./disp_profile');
app.use('/disp_profile',disp_profile);


var add_menu = require('./add_menu');
app.use('/add_menu',add_menu);

var disp_menu = require('./disp_menu');
app.use('/disp_menu',disp_menu);

var placed_orders = require('./placed_orders');
app.use('/placed_orders',placed_orders);

var remove_menu = require('./remove_menu');
app.use('/remove_menu',remove_menu);

app.listen(8080, (req, res) => {
  console.log("Listening on 8080");
});
module.exports=router;