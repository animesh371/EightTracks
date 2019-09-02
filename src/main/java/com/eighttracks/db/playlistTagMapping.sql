create table playlistTagMapping (
    playlistId INTEGER REFERENCES playlist(playlistId) ON DELETE CASCADE,
    tagId INTEGER REFERENCES tags(tagId) ON DELETE CASCADE
);