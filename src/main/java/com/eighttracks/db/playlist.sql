create table playlists (
    playlistId SERIAL PRIMARY KEY,
    playlistName varchar(200),
    playcount INTEGER DEFAULT 0,
    likeCount INTEGER DEFAULT 0,
    trackCount INTEGER,
    songs jsonb,
    notes text,
    createdAt timestamp
);