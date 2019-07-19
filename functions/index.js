
const functions = require('firebase-functions');
var cors = require('cors')({ origin: true });

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.offlineObserveFromDevice = functions.database.ref("/SI_ALERT/{device_number}").onWrite((change,context)=>{
        

})

/*
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

var user = functions.database.ref();*/

exports.sendFriendRequest = functions.https.onRequest((req, res) => {
    cors(req, res, () => {
        var senderId = req.body.sender_id;
        var receiverId = req.body.receiver_id;
        var notificationTypeId = req.body.notification_type_id;
        var userName = req.body.userName;
        var notificationToken = "";

        admin.database().ref('/users/' + receiverId).once("value", function (snap) {
            if (snap.exists) {
                notificationToken = snap.child('notification_token').val()

                var payload = {

                    notification: {
                        title: "SI friend request",
                        body: userName + " has sent you SI friend request"

                    },
                    data: {
                        username: userName,
                        sender_id: senderId,
                        receiver_id: receiverId,
                        notification_type_id: notificationTypeId
                    }
                };
                if (notificationToken !== "") {
                    var newData = {
                        "notification_type_id": notificationTypeId,
                        "sender_id": senderId,
                        "message": userName + " has sent you friend request."
                    }
                    admin.database().ref("Notification").child(receiverId).push(newData)
                    return admin.messaging().sendToDevice(notificationToken, payload)
                        .then(response => {
                            console.log("Successfully sent message: ", response);
                            res.status(200).send("Success")
                            return ""
                        });
                } else {
                    console.log("Error")
                    res.status(301).send("Failure")
                }

            }else{
                res.status(301).send("Failure")
            }
        })

    })
})


exports.sendAlertMessages = functions.https.onRequest((req,res)=>{
    cors(req,res,()=>{
        var senderId = req.body.sender_id
        var receiverId = req.body.receiver_id
        var noticationTypeId = req.body.notification_type_id
        var userName = req.body.userName
        var receiverIds = []
        var registrationTokens = []

        admin.database().ref('/friends/'+senderId).once('value',function(snap){
            if(snap.exists){
                snap.forEach((child)=>{
                    receiverIds.push(child.val().id)
                })

                receiverIds.forEach((id)=>{
                    admin.database().ref('/users/'+id).once('value',function(snap){
                        if(snap.child('notification_token').val()!==null){
                            registrationTokens.push(snap.child('notification_token').val())
                        }else{
                            registrationTokens.push("")
                        }
                        if(receiverIds.length===registrationTokens.length){
                            res.status(200).json(registrationTokens)
                        }
                    })
                })
            }else{
                res.status(301).send("error")
            }
        })

    })
})

exports.acceptDenyInvitation = functions.https.onRequest((req, res) => {
    cors(req, res, () => {
        var senderId = req.body.sender_id;
        console.log("senderId", senderId);
        var receiverId = req.body.receiver_id;
        console.log("receiverId",receiverId)
        var notificationTypeId = req.body.notification_type_id;
        var accept = req.body.accept
        var userName = req.body.senderUserName;
        var notificationToken = "";
        var message = ""
       

        admin.database().ref('/users/' + receiverId).once("value", function (snap) {
            if (snap.exists) {
                notificationToken = snap.child('notification_token').val()
                if (accept) {
                    message = userName + " has accepted your request."
                } else {
                    message = userName + " has denied your request."
                }

                var payload = {

                    notification: {
                        title: "SI Invitation Reply",
                        body: message

                    },
                    data: {
                        username: userName,
                        sender_id: senderId,
                        receiver_id: receiverId,
                        notification_type_id: notificationTypeId
                    }
                };
                console.log("notificationtoken", notificationToken)
                if (notificationToken !== "") {
                    var newData = {
                        "notification_type_id": notificationTypeId,
                        "sender_id": senderId,
                        "message": message
                    }
                    admin.database().ref("Notification").child(receiverId).push(newData)
                    return admin.messaging().sendToDevice(notificationToken, payload)
                        .then(response => {
                            console.log("Successfully sent message: ", response);
                            res.status(200).send("Success")
                            return ""
                        });
                } else {
                    console.log("Error")
                }

            } else {
                console.log("error")
            }

        })




    })
})



/*exports.testingApi = functions.https.onRequest((req,res) =>{
    cors(req,res,()=>{
        admin.database().ref('/friends/').child(req.body.id).orderByChild('status').equalTo(2). once("value",function(snap){

            console.log(req.body.id)
               console.log(snap.val());
               snap.forEach((child)=>{
                   console.log(child.key,child.val());
               })

               res.status(200).json(req.body.id);
           })

    })
    //res.status(200).json(user);
})*/


