const admin = require('firebase-admin');
const db = admin.firestore();

module.exports.getAlbum = (req, res) => {
    const album =[]
    db.collection("Album").limit(3).get()
    .then(snapshot=>{
        snapshot.forEach(doc=>{
            album.push(doc.data())
        })
        return res.status(200).send({message:"Thành Công",data:album})
    })
    .catch(Error=>{
        return res.send({message:Error.message,data:[]})
    })
}