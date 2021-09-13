const cookieParser = require("cookie-parser");
const csrf = require("csurf");
const bodyParser = require("body-parser");
const admin = require("firebase-admin");

const serviceAccount = require("./humanresourcemanager-57f05-firebase-adminsdk-k6bpj-8d4d470aa8.json");


admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://humanresourcemanager-57f05.firebaseio.com"
  });

  const csrfMiddleware = csrf({ cookie: true });

// Load Node modules
var express = require('express');
const ejs = require('ejs');
// Initialise Express
var app = express();
app.use(bodyParser.json());
app.use(cookieParser());
app.use(csrfMiddleware);
app.all("*", (req, res, next) => {
    res.cookie("XSRF-TOKEN", req.csrfToken());
    next();
  });
// Set the view engine to ejs
app.set('view engine', 'ejs');
// Render static files
app.use(express.static('public'));

// Set the view engine to ejs
app.set('view engine', 'ejs');

app.get('/index', function (req, res) {
    const sessionCookie = req.cookies.session || "";

  admin
    .auth()
    .verifySessionCookie(sessionCookie, true /** checkRevoked */)
    .then(() => {
      res.render('page/index');
    })
    .catch((error) => {
      res.redirect('/');
    });
});
app.get('/album', function (req, res) {
    const sessionCookie = req.cookies.session || "";

    admin
      .auth()
      .verifySessionCookie(sessionCookie, true /** checkRevoked */)
      .then(() => {
        res.render('page/album');
      })
      .catch((error) => {
        res.redirect('/');
      });
    
});
app.get('/song', function (req, res) {
    const sessionCookie = req.cookies.session || "";

    admin
      .auth()
      .verifySessionCookie(sessionCookie, true /** checkRevoked */)
      .then(() => {
        res.render('page/song');
      })
      .catch((error) => {
        res.redirect('/');
      });
    
});

app.get('/user', function (req, res) {
    const sessionCookie = req.cookies.session || "";

    admin
      .auth()
      .verifySessionCookie(sessionCookie, true /** checkRevoked */)
      .then(() => {
        res.render('page/user');
      })
      .catch((error) => {
        res.redirect('/');
      });
    
});
app.get('/', function (req, res) {
    res.render('page/login');
    
});


app.post("/sessionLogin", (req, res) => {
    const idToken = req.body.idToken.toString();
  
    const expiresIn = 60 * 60 * 24 * 5 * 1000;
  
    admin
      .auth()
      .createSessionCookie(idToken, { expiresIn })
      .then(
        (sessionCookie) => {
          const options = { maxAge: expiresIn, httpOnly: true };
          res.cookie("session", sessionCookie, options);
          res.end(JSON.stringify({ status: "success" }));
        },
        (error) => {
          res.status(401).send("UNAUTHORIZED REQUEST!");
        }
      );
  });
  
  app.get("/sessionLogout", (req, res) => {
    res.clearCookie("session");
    res.redirect("/");
  });
  


// Port website will run on
app.listen(8080);