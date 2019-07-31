
const functions = require('firebase-functions');
var cors = require('cors')({ origin: true });
var http = require('http')

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.offlineObserveFromDevice = functions.database.ref("/SI_ALERT/{device_number}").onWrite((change,context)=>{
        

})

    var options = {
      'method': 'POST',
      'hostname': 'api.sparrowsms.com',
      'path': '/v2/sms',
      'headers': {
          'Content-Type':'application/json'
      }
    };

exports.sendMessages = functions.https.onRequest((reqs,res)=>{
    

    var postData = {
        to:"+9779849276763",
        from:"Demo",
        token:"uYDHxBwMEwsha3Sldmpu",
        text:"ljdfkkl"
    }
    
    var req = http.request(options, function (res) {
      var chunks = [];
    
      res.on("data", function (chunk) {
        chunks.push(chunk);
      });
    
      res.on("end", function (chunk) {
        var body = Buffer.concat(chunks);
        console.log(body.toString());
        
      });
    
      res.on("error", function (error) {
        console.error(error);
        res.status(301).json(error)
        
      });
    });
    
    //var postData =  "{ \n\"to\":\"+9779849276763\",\n\"from\":\"Demo\",\n        \"text\":\"This is texting\",\n        \"token\":\"uYDHxBwMEwsha3Sldmpu\"\n}";
    
    req.write(JSON.stringify(postData));
    
    req.end();
    })

exports.sendFriendRequest = functions.https.onRequest((req, res) => {
    cors(req, res, () => {
        var senderId = req.body.sender_id
        console.log("SI friend Request",senderId)
        var receiverId = req.body.receiver_id
        console.log("SI frind request",receiverId)
        var notificationTypeId = "1";
        var userName = req.body.userName;
        console.log("SI Friend request",userName)
        var notificationToken = "";
        admin.database().ref('/users/' + receiverId).once("value",async function (snap) {
            if (snap.exists) {
                notificationToken = snap.child('notification_token').val()
                console.log("SI friend request",notificationToken)
                var payload = {

                    notification: {
                        title: "SI friend request",
                        body: capitalizeFirstLetter(userName) + " has sent you SI friend request"
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
                        "title":"SI friend request",
                        "message": userName + " has sent you friend request.",
                        "timeStamp": new Date().getTime()
                    }
                    admin.database().ref("Notification").child(receiverId).push(newData)
                    const response = await admin.messaging().sendToDevice(notificationToken, payload);
                    console.log("Successfully sent message: ", response);
                    res.status(200).json({
                        statusCode: 200,
                        message: "Success"
                    });
                    return "";
                } else {
                    console.log("Error")
                    res.status(301).json({
                        statusCode: 301,
                        message: "Failure"
                    })
                }

            }else{
                res.status(301).json({
                    statusCode: 301,
                    message: "Failure"
                })
            }
        },function(error){
            console.log("SI friend request",error.message)
            res.status(301).json({
                statusCode:301,
                message: "Error"
            })
        })

    })
})

exports.sendSafeAlert = functions.https.onRequest((req,res)=>{
    cors(req,res,()=>{
        var senderId = req.body.sender_id
        var notificationTypeId = "4"
        var userName = req.body.userName
        var receiverIds = []
        var message = ""
        var registrationTokens = []
        var payload = {
            notification:{
                title: "SI safe alert",
                body: " Your SI friend "+capitalizeFirstLetter(userName)+ " is safe now."
            },
            data:{
                username: userName,
                sender_id: senderId,
                notification_type_id: notificationTypeId,
            }
        }

        admin.database().ref('/friends/'+senderId).once('value',function(snap){
            if(snap.exists){
                snap.forEach((child)=>{
                    receiverIds.push(child.val().number)
                })

                receiverIds.forEach((id)=>{
                    admin.database().ref('/users/'+id).once('value',async function(snap){
                        if(snap.child('notification_token').val()!==null && snap.child('notification_token').val()!==""){
                            registrationTokens.push(snap.child('notification_token').val())
                        }else{
                            registrationTokens.push("kjgkjjk")
                        }
                        console.log(registrationTokens)
                        if(receiverIds.length===registrationTokens.length){
                            const response=  admin.messaging().sendToDevice(registrationTokens, payload)
                                console.log("Successfully sent message: ", response);
                                receiverIds.forEach((id)=>{
                                    var newData = {
                                        "notification_type_id": notificationTypeId,
                                        "sender_id": senderId,
                                        "title":"SI safe alert",
                                        "message": "Your SI Friend "+userName + " is safe now",
                                        "timeStamp": new Date().getTime()
                                    }
                                    admin.database().ref("Notification").child(id).push(newData)
                                })
                                res.status(200).json({
                                    statusCode: 200,
                                    message: "Success"
                                })
                        }
                    })
                })
            }else{
                res.status(301).json({
                    statusCode:301,
                    message: "Failure"
                })
            }
        })
    })
})


exports.sendAlertMessages = functions.https.onRequest((req,res)=>{
    cors(req,res,()=>{
        var senderId = req.body.sender_id
        var notificationTypeId = "2"
        var userName = req.body.userName
        var geoLatitude = req.body.latitude
        var geoLongitude = req.body.longitude
        var receiverIds = []
        var registrationTokens = []
        var payload = {
            notification: {
                title: "SI Emergency Alert",
                body: "Your SI Friend "+capitalizeFirstLetter(userName) + " is in need of help. Current location : Latitude="+geoLatitude+
                "Longitude="+geoLongitude
            },
            data: {
                username: userName,
                sender_id: senderId,
                notification_type_id: notificationTypeId,
                latitude: geoLatitude,
                longitude: geoLongitude
            }
        };

        admin.database().ref('/friends/'+senderId).once('value',function(snap){
            if(snap.exists){
                snap.forEach((child)=>{
                    receiverIds.push(child.val().number)
                })

                receiverIds.forEach((id)=>{
                    admin.database().ref('/users/'+id).once('value',async function(snap){
                        if(snap.child('notification_token').val()!==null && snap.child('notification_token').val()!==""){
                            registrationTokens.push(snap.child('notification_token').val())
                        }else{
                            registrationTokens.push("kjgkjjk")
                        }
                        console.log(registrationTokens)
                        if(receiverIds.length===registrationTokens.length){
                            const response = await admin.messaging().sendToDevice(registrationTokens, payload)
                                console.log("Successfully sent message: ", response);
                                receiverIds.forEach((id)=>{
                                    var newData = {
                                        "notification_type_id": notificationTypeId,
                                        "sender_id": senderId,
                                        "title":"SI emergency alert",
                                        "message": "Your SI Friend "+capitalizeFirstLetter(userName)  + " is in need of help. Current location : Latitude="+geoLatitude+
                                        " Longitude="+geoLongitude,
                                        "timeStamp": new Date().getTime()
                                    }
                                    admin.database().ref("Notification").child(id).push(newData)
                                })
                                res.status(200).json({
                                    statusCode: 200,
                                    message: "Success"
                                })
                        }
                    })
                })
            }else{
                res.status(301).json({
                    statusCode:301,
                    message: "Failure"
                })
            }
        },function(error){
            console.log("Send alert Message","Error")
            res.status(301).json({
                statusCode: 301,
                message: "Failure"
            })
        })

    })
})

exports.acceptDenyInvitation = functions.https.onRequest((req, res) => {
    cors(req, res, () => {
        var senderId = req.body.sender_id;
        console.log("senderId", senderId);
        var receiverId = req.body.receiver_id;
        console.log("receiverId",receiverId)
        var notificationTypeId = "";
        var status = req.body.status
        var userName = req.body.senderUserName;
        var notificationToken = "";
        var message = ""
       

        admin.database().ref('/users/' + receiverId).once("value", function (snap) {
            if (snap.exists) {
                notificationToken = snap.child('notification_token').val()
                if(status>2){
                    admin.database().ref('/friends/'+receiverId).child(senderId).remove(then=>{
                        admin.database().ref('/friends/'+senderId).child(receiverId).remove(then=>{
                            console.log("Deleted")
                            res.status(200).json({
                                statusCode: 200,
                                message: "Success"
                            })
                        })
                    })
                }else{
                    
                    if(status===1){
                        notificationTypeId="3"
                        message = capitalizeFirstLetter(userName)  + " has accepted your request."
                        admin.database().ref('/friends/'+receiverId).child(senderId).update({
                            status:1,
                            notification:true
        
                        })
                        admin.database().ref('/friends/'+senderId).child(receiverId).update({
                            status:1,
                            notification:true
                        })

                    }else{
                        message = capitalizeFirstLetter(userName)  + " has denied your request."
                        notificationTypeId="4"
                        admin.database().ref('/friends/'+receiverId).child(senderId).remove(then=>{
                            admin.database().ref('/friends/'+senderId).child(receiverId).remove(then=>{
                                console.log("Deleted")
                                res.status(200).json({
                                    statusCode: 200,
                                    message: "Success"
                                })
                            })
                        })
                    }
                 
                }

                var payload = {

                    notification: {
                        title: "SI friend request reply",
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
                if(status>2){
                    res.status(200).json({
                        statusCode: 200,
                        message: "Success"
                    })
                }else{
                    if (notificationToken !== "") {

                        var newData = {
                            "notification_type_id": notificationTypeId,
                            "sender_id": senderId,
                            "title":"SI friend request reply",
                            "message": message,
                            "timeStamp": new Date().getTime()
                        }
                        admin.database().ref("Notification").child(receiverId).push(newData)
                        return admin.messaging().sendToDevice(notificationToken, payload)
                            .then(response => {
                                console.log("Successfully sent message: ", response);
                                res.status(200).json({
                                    statusCode:200,
                                    message: "Success"
                                })
                                return ""
                            });
                    } else {
                        console.log("Error")
                        res.status(301).json({
                            statusCode: 301,
                            message: "Failure"
                        })
                    }
                }
              

            } else {
                console.log("error")
                res.status(301).json({
                    statusCode: 301,
                    message: "Failure"
                })
            }

        })
    })
})

exports.sendOfflineSafeAlert = functions.https.onRequest((req,res)=>{
    cors(req,res,()=>{
        var senderId = req.body.sender_id
        var deviceId = req.body.device_id
        admin.database().ref('/users/'+senderId).once('value',function(snap){
            if(snap.exists){
                var notificationTypeId = "4"
                var userName = snap.child('username').val()
                var receiverIds = []
                var message = ""
                var registrationTokens = []
                var payload = {
                    notification:{
                        title: "SI safe alert",
                        body: " Your SI friend "+capitalizeFirstLetter(userName)+ " is safe now."
                    },
                    data:{
                        username: userName,
                        sender_id: senderId,
                        notification_type_id: notificationTypeId,
                    }
                }
        
                admin.database().ref('/friends/'+senderId).once('value',function(snap){
                    if(snap.exists){
                        snap.forEach((child)=>{
                            receiverIds.push(child.val().number)
                        })
        
                        receiverIds.forEach((id)=>{
                            admin.database().ref('/users/'+id).once('value',function(snap){
                                if(snap.child('notification_token').val()!==null && snap.child('notification_token').val()!==""){
                                    registrationTokens.push(snap.child('notification_token').val())
                                }else{
                                    registrationTokens.push("kjgkjjk")
                                }
                                console.log(registrationTokens)
                                if(receiverIds.length===registrationTokens.length){
                                    return admin.messaging().sendToDevice(registrationTokens, payload)
                                    .then(response => {
                                        console.log("Successfully sent message: ", response);
                                        receiverIds.forEach((id)=>{
                                            var newData = {
                                                "notification_type_id": notificationTypeId,
                                                "sender_id": senderId,
                                                "title":"SI safe alert",
                                                "message": "Your SI Friend "+userName + " is safe now",
                                                "timeStamp": new Date().getTime()
                                            }
                                            admin.database().ref("Notification").child(id).push(newData)
                                        })
                                        res.status(200).json({
                                            statusCode: 200,
                                            message: "Success"
                                        })
                                        return ""
                                    });
                                }
                            })
                        })
                    }else{
                        res.status(301).json({
                            statusCode:301,
                            message: "Failure"
                        })
                    }
                })
            }else{
                res.status(301).json({
                    statusCode: 301,
                    message: "Failure"
                })
            }
        })
      
    })
})


exports.sendOfflineAlertMessages = functions.https.onRequest((req,res)=>{
    cors(req,res,()=>{

        var senderId = req.body.sender_id
        var notificationTypeId = "2"
        var geoLatitude = req.body.latitude
        var geoLongitude = req.body.longitude
        admin.database().ref('/users/'+senderId).once('value',function(snap){
            if(snap.exists){
                var userName = snap.child('username').val()
                var receiverIds = []
        var registrationTokens = []
        var payload = {
            notification: {
                title: "SI Emergency Alert",
                body: "Your SI Friend "+capitalizeFirstLetter(userName) + " is in need of help. Current location : Latitude="+geoLatitude+
                "Longitude="+geoLongitude
            },
            data: {
                username: userName,
                sender_id: senderId,
                notification_type_id: notificationTypeId,
                latitude: geoLatitude,
                longitude: geoLongitude
            }
        };

        admin.database().ref('/friends/'+senderId).once('value',function(snap){
            if(snap.exists){
                snap.forEach((child)=>{
                    receiverIds.push(child.val().number)
                })

                receiverIds.forEach((id)=>{
                    admin.database().ref('/users/'+id).once('value',function(snap){
                        if(snap.child('notification_token').val()!==null && snap.child('notification_token').val()!==""){
                            registrationTokens.push(snap.child('notification_token').val())
                        }else{
                            registrationTokens.push("kjgkjjk")
                        }
                        console.log(registrationTokens)
                        if(receiverIds.length===registrationTokens.length){
                            return admin.messaging().sendToDevice(registrationTokens, payload)
                            .then(response => {
                                console.log("Successfully sent message: ", response);
                                receiverIds.forEach((id)=>{
                                    var newData = {
                                        "notification_type_id": notificationTypeId,
                                        "sender_id": senderId,
                                        "title":"SI emergency alert",
                                        "message": "Your SI Friend "+capitalizeFirstLetter(userName)  + " is in need of help. Current location : Latitude="+geoLatitude+
                                        " Longitude="+geoLongitude,
                                        "timeStamp": new Date().getTime()
                                    }
                                    admin.database().ref("Notification").child(id).push(newData)
                                })
                                res.status(200).json({
                                    statusCode: 200,
                                    message: "Success"
                                })
                                return ""
                            });
                        }
                    })
                })
            }else{
                res.status(301).json({
                    statusCode:301,
                    message: "Failure"
                })
            }
        })


            }else{

                res.status(301).json({
                    statusCode:301,
                    message: "Failure"
                })
            }

        })
            
    })
})



function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

