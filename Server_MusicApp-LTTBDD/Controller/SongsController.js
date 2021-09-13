const admin = require('firebase-admin');
const db = admin.firestore();

module.exports.getRank = (req, res) => {
    const music =[]
    db.collection("Songs").orderBy('Like','desc').limit(5).get()
    .then(snapshot=>{
        snapshot.forEach(doc=>{
            music.push(doc.data())
        })
        return res.send({message:"Thành Công",data:music})
    })
    .catch(Error=>{
        return res.send({message:Error.message,data:[]})
    })
}

module.exports.suggestSong = (req, res) => {
    const all = []
    db.collection("Songs").get()
    .then(snapshot=>{
        snapshot.forEach(doc=>{
            all.push(doc.data())
        })
        let random = getRandom(all)
        random.length = 4
        return res.send({message:"Thành Công",data:random})
    })
    .catch(Error=>{
        return res.send({message:Error.message,data:[]})
    })
}

//Lấy toàn bộ bài hát theo danh sách================================================================
module.exports.getSongFromList = async (req,res)=>{
    const getSongsList = async (ID) =>{
        const snapshot = await db.collection("Songs").where("ID","==",ID).get()
        if(!snapshot.empty){
            return snapshot.docs[0].data()
        }else{
            throw new Error('Not Found')
        }   
    }
    const params = req.body.data
    let promise = params.map(
        songID=>getSongsList(songID)
            .then(songInfo=>{
                return songInfo
            })
            .catch(err=>{
                return err
            })
    )
    return Promise.all(promise)
        .then((data)=>res.status(200).json({data: data}))
        .catch(err=>console.log(err, "lỗi"))
}

module.exports.searchMusic =(req,res) => {
    const music =[]
    db.collection("Songs").orderBy('Name').startAt(req.query.name).endAt(req.query.name+"\uf8ff").get()
    .then(snapshot=>{
        snapshot.forEach(doc=>{
            music.push(doc.data())
        })
        return res.send({message:"Thành Công",data:music})
    })
    .catch(Error=>{
        return res.send({message:Error.message,data:[]})
    })
}

function getRandom(array){
    var counter = array.length, index, temp
    while ((counter--)){
        index = Math.random()*counter|0
        temp = array[counter]
        array[counter]= array[index]
        array[index] = temp
    }
    return array;
}