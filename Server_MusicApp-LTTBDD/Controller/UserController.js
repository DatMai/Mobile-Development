const admin = require('firebase-admin');
const db = admin.firestore();

module.exports.Signup = (req,res) => {
    let data = {
        "Username":req.body.Username,
        "Password":req.body.Password,
        "Email":req.body.Email,
        "ID":req.body.ID,
        "linkFaceBook":false,
        "linkGmail":false,
        "Favorites":[]
    }
    db.collection("Users").doc(req.body.ID).set(data)
    .then(()=>{
        return res.send({message:"Đăng Ký Thành Công",data:[]})
    })
    .catch(Error=>{
        return res.send({message:Error.message,data:[]})
    })
}

module.exports.Login = (req, res) => {
    db.collection("Users").where("Username","==",req.body.Username).get()
    .then(snapshot=>{
        if(snapshot.empty)throw new Error("Không Tồn Tại Tài Khoản")
        snapshot.forEach(doc=>{
            if(req.body.Password === doc.data().Password){
                admin.auth().createCustomToken("CU001")
                .then(customtoken=>{
                    return res.send({message:"Thành Công", data:customtoken})
                })
                .catch(Error=>{
                    return res.send({message:Error.message,data:[]})
                })
            }
            else{
                return res.send({message:"Sai Mật Khẩu",data:[]})
            }
        })
    })
    .catch(Error=>{
        return res.send({message:Error.message,data:[]})
    })
}

module.exports.changePassword = (req, res) => {
    db.collection("Users").doc(req.body.UserID).update({Password:req.body.newPassword})
    .then(()=>{
        return res.send({message:"Sửa Thành Công",data:[]})
    })
    .catch(Error=>{
        return res.send({message:Error.message,data:[]})
    })
}

module.exports.getUserInfo = (req, res) => {
    const userID = req.query.userID
    const data=[]
    db.collection("Users").where("ID","==",userID).get()
    .then(snapshot=>{
        snapshot.forEach(doc=>{
            data.push(doc.data())
        })
        return res.send({message:"Thành Công",data:data})
    })
    .catch(Error=>{
        return res.send({message:Error.message,data:[]})
    })
}

module.exports.changeStatusLink = (req, res) => {
    let status = req.query.status==="true"?true:false
    if(req.query.mail==="true"){
        db.collection("Users").doc(req.body.UserID).update({linkGmail:status})
        .then(()=>{
            return res.send({message:"Sửa Thành Công",userdata:[]})
        })
        .catch(Error=>{
            return res.send({message:Error.message,userdata:[]})
        })
    }else{
        db.collection("Users").doc(req.body.UserID).update({linkFaceBook:status})
        .then(()=>{
            return res.send({message:"Sửa Thành Công",userdata:[]})
        })
        .catch(Error=>{
            return res.send({message:Error.message,userdata:[]})
        })
    }
}