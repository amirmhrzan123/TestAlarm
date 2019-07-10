

const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.notifyNewMessage = functions.database.ref("/Notification/{userId}").onWrite((change,context)=>{
  
    var payload = {

        notification:{
            title:"Test",
            body:"test"

        },
        data:{
            username:"test",
            email:"test"
        }
    };
    return admin.messaging().sendToDevice("e7nKHHhceZ4:APA91bFmaU7QOhFg-6o5e8ZAiiXFkgcov9udYabOR8mNLYsMNpFDuJrL8YfzMEBJAC5yoRqahmnG94f8C9e15SDSzXtQpHRoojoICRWzdW7A8BaRep_yqFmtbkF2GyWiC5LCZ627KX2m",payload)
    .then(response=>{
        console.log("Successfully sent message: ",response);
        return null
    });


});

let user = functions.database.ref('/users');

exports.testApi = functions.https.onRequest((req,res) =>{
    res.status(200).json(user);
})
