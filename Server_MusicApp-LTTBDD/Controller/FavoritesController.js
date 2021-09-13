const admin = require('firebase-admin');
const db = admin.firestore();

// Thêm bài hát vào danh sách yêu thích============================================================
module.exports.addNew = async (req,res) =>{
    const userID = req.query.userID
    const songID = req.query.songID
    //Thêm mã bài hát vào danh sách của ng dùng
    const addToFavoriteList = async () => {
        await db.collection('Users')
            .doc(userID)
            .update({Favorites:admin.firestore.FieldValue.arrayUnion(songID)})
    }
    //Tăng số lượt thích lên 1
    const incrementLike = async () => {
        await db.collection("Songs").doc(songID)
            .update({Like:admin.firestore.FieldValue.increment(1)})
    }

    await getUserInfo(userID)
        .then(async userInfo=>{
            if(userInfo.Favorites.filter(song_id => song_id === songID ).length>0){
                throw new Error('Bài Hát Đã Tồn Tại')
            }else{
                return addToFavoriteList()
            }
        })
        .then(()=>incrementLike())
        .then(()=>res.status(200).send({message:"Success"}))
    .catch(error=>{
        console.log(error)
        return res.status(500).send(error) 
    })
}

//Xoá Bài Hát Yêu Thích============================================================================
module.exports.remove = async (req,res)=>{
    const userID = req.query.userID;
    const songID = req.query.songID;
    //Xoá mã bài hát khỏi danh sách của ng dùng
    const removeFromFavoriteList = async () =>{
        await db.collection("Users").doc(userID)
            .update({Favorites:admin.firestore.FieldValue.arrayRemove(songID)})
    }
    //Giảm số lượt thích đi 1
    const decrementLike = async () => {
        db.collection("Songs").doc(songID)
            .update({Like:admin.firestore.FieldValue.increment(-1)})
    }

    await getUserInfo(userID)
    .then(async userInfo=>{
        if(userInfo.Favorites.filter(song_id => song_id === songID ).length===0){
            throw new Error('Bài Hát Không Tồn Tại')
        }else{
            return removeFromFavoriteList()
        }
    })
    .then(()=>decrementLike())
    .then(()=>res.status(200).send({message:"Success"}))
.catch(error=>{
    console.log(error)
    return res.status(500).send(error) 
})
}



const getUserInfo = async (userID) =>{
    const snapshot = await db.collection("Users").where("ID","==",userID).get()
    if(!snapshot.empty){
        return snapshot.docs[0].data()
    }else{
        throw new Error('User Not Found')
    }
}