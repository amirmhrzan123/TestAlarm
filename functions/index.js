
const functions = require('firebase-functions');
var cors = require('cors')({origin: true});

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

var user = functions.database.ref();

exports.sendFriendRequest = functions.https.onRequest((req,res)=>{
    cors(req,res,()=>{
        var senderId = req.body.sender_id;
        var receiverId = req.body.receiver_id;
        var notificationTypeId = req.body.notification_type_id;
        var userName = "";
        var notificationToken = "";
    
         admin.database().ref('/users').orderByChild('id').equalTo(senderId).once("value",function(snap){
             if(snap.exists){
                snap.forEach((child)=>{
                    if(child.key=="username"){
                        userName = child.val();
                    }else if(child.key=="notification_token"){
                        notificationToken = child.val();
                    }
                })
             }else{

             }
          
        })
        
        var payload = {

            notification:{
                title:"SI friend request",
                body:username+" has sent you SI friend request";
    
            },
            data:{
                username:userName,
                sender_id : senderId,
                receiver_id : receiverId,
                notification_type_id : notificationTypeId
            }
        };
        if(notificationToken!=""){
            admin.database().ref("/Notification").child(receiverId).push.set({
                "notification_type_id":notificationTypeId,
                "sender_id":senderId,
                "message":userName+ " has sent you friend request.",
                
            })
            return admin.messaging().sendToDevice(notificationToken,payload)
            .then(response=>{
                console.log("Successfully sent message: ",response);
                res.status(200).send("Success")
                return ""
            });
        }else{

        }
    })
})


exports.acceptDenyInvitation = functions.https.onRequest((req,res)=>{
    cors((req,res)=>{
        var senderId = req.body.sender_id
        var receiverId = req.body.receiver_id
        var accept = req.body.accept
        
    }
})


exports.testingApi = functions.https.onRequest((req,res) =>{
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
})


