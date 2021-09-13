const admin = require('firebase-admin');
const db = admin.firestore();

module.exports.getFirst = (req,res)=>{
    const kindID = req.query.id
    let music=[]
    db.collection('Songs').orderBy("ID","asc").where("Type","==",kindID).limit(7).get()
    .then(snapshot=>{
        snapshot.forEach(docs=>{
            music.push(docs.data())
        })
        return res.status(200).send({message:"Thành Công", data:music})
    })
    .catch(Error=>{
        return res.status(500).send({message:Error.message, data:[]})
    })
}

module.exports.getNext = (req,res) => {
    const kindID = req.query.id;
    const lastmusicID = req.query.lastid
    let music=[]
    db.collection("Songs").orderBy("ID","asc").where("Type","==",kindID).startAfter(lastmusicID).limit(5).get()
    .then(snapshot=>{
       snapshot.forEach(doc=>{
            music.push(doc.data())
       })
       return res.status(200).send({data:music})
    })
    .catch(Error=>{
        return res.status(500).send({message:Error.message})
    })
}