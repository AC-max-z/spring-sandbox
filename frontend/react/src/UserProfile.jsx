const UserProfile = ({ name, age, gender, imageId, ...props }) => {
    gender = gender === 'MALE' ? 'men' : 'women';
    const profilePicLink = `https://randomuser.me/api/portraits/${gender}/${imageId}.jpg`;
    return (
        <div>
            <h1>{name}</h1>
            <p>{age}</p>
            <img src={profilePicLink}></img>
            {props.children}
        </div>
    );
};

export { UserProfile };
