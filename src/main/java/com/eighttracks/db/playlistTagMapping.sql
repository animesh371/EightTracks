create table playlistTagMapping (
    playlistId INTEGER REFERENCES playlists(playlistId) ON DELETE CASCADE,
    tagId INTEGER REFERENCES tags(tagId) ON DELETE CASCADE
);