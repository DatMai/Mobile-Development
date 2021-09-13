const admin = require('firebase-admin');
const db = admin.firestore();

//Lấy toàn bộ playlist của ng dùng=================================================================
module.exports.getUserPlaylists = async (req, res) => {
    const userID = req.query.userID;
    await getUserPlaylist(userID)
    .then(userPlaylist=>res.send({message:" Get Playlist Success",data:userPlaylist}))
    .catch(Error=> res.send({message:Error.message,data:[]}))
}

//Thêm mới playlist===============================================================================
module.exports.createNew = (req,res) => {
    const userID = req.query.userID;
    const playslitName = req.query.Name;

    let ID = "PL"+ userID + createID()
    let data = {
        ID:ID,
        Name:playslitName,
        "Deleted":false,
        "Songs":[]
    }

    db.collection("Users").doc(userID).collection("MyPlayList").doc(ID).set(data)
        .then(async()=>{
            await getUserPlaylist(userID)
            .then(userPlaylist=>res.send({message:" Get Playlist Success",data:userPlaylist}))
            .catch(Error=> res.send({message:Error.message,data:[]}))
        }) 
        .catch(Error=>{
            return res.send({message:Error.message,data:[]})
        })
}

//Đổi tên playlist==================================================================================
module.exports.renamePlaylist = (req,res) =>{
    const userID = req.query.userID;
    const newPlayslitName = req.query.Name;
    const playlistID = req.query.playListID;

    db.collection("Users").doc(userID).collection("MyPlayList").doc(playlistID).update({Name:newPlayslitName})
    .then(async()=> {
        await getUserPlaylist(userID)
            .then(userPlaylist=> res.send({message:" Get Playlist Success",data:userPlaylist}))
            .catch(Error=> res.send({message:Error.message,data:[]}))}
    )
    .catch(Error=> res.send({message:Error.message,data:[]}))
}

//Xoá Playlist====================================================================================
module.exports.deletePlaylist = (req, res) => {
    const userID = req.query.userID;
    const playlistID = req.query.playListID;

    db.collection("Users").doc(userID).collection("MyPlayList").doc(playlistID).update({Deleted:true})
    .then(async()=> {
        await getUserPlaylist(userID)
            .then(userPlaylist=> res.send({message:" Get Playlist Success",data:userPlaylist}))
            .catch(Error=> res.send({message:Error.message,data:[]}))}
    )
    .catch(Error=> res.send({message:Error.message,data:[]}))
}

//Thêm bài hát vào playlist========================================================================
module.exports.addItem = async (req, res) => {
    const userID = req.query.userID;
    const playlistID = req.query.playListID;
    const songID = req.query.ID;

    const addSongToPlaylist = async () => {
        await db.collection("Users").doc(userID)
        .collection("MyPlayList").doc(playlistID)
        .update({Songs:admin.firestore.FieldValue.arrayUnion(songID)})
    }

    await getSongsFromPlaylist(userID, playlistID)
    .then(songs=>{
        if(songs.filter(item=>item === songID).length > 0){
            throw new Error('Bài hát đã tồn tại')
        }else{
            return addSongToPlaylist();
        }
    })
    .then(()=>res.status(200).send({message:'success'}))
    .catch(err=>{
        return res.status(500).send({message:err})
    });
}

//Xoá bài hát khỏi playlist========================================================================
module.exports.removeItem = async (req,res) => {
    const userID = req.query.userID;
    const playlistID = req.query.playListID;
    const songID = req.query.ID;

    const removeSongToPlaylist = async () => {
        await db.collection("Users").doc(userID)
        .collection("MyPlayList").doc(playlistID)
        .update({Songs:admin.firestore.FieldValue.arrayRemove(songID)})
    }

    await getSongsFromPlaylist(userID, playlistID)
    .then(songs=>{
        if(songs.filter(item=>item === songID).length === 0){
            throw new Error('Bài hát không tồn tại')
        }else{
            return removeSongToPlaylist();
        }
    })
    .then(()=>res.status(200).send({message:'success'}))
    .catch(err=>{
        return res.status(500).send({message:err})
    });
}

//=================================================================================================//
const getSongsFromPlaylist = async (userID, playlistID)=>{
   const playlist = await db.collection("Users").doc(userID)
    .collection("MyPlayList").doc(playlistID).get()
    if(playlist){
        return playlist.data().Songs
    }else{
        throw new Error('Playlist not found')
    }
}

function createID(){
    return Math.random().toString(36).substr(2, 5).toUpperCase();
}

const getUserPlaylist = async (userID) =>{
    let list = []
    const snapshot = await db.collection("Users").doc(userID).collection("MyPlayList").where("Deleted","==",false).get()
   if(!snapshot.empty){
       snapshot.forEach(item=>list.push(item.data()))
       return list;
   }else{
       throw new Error('Not Found Playlist')
   }
}