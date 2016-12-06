# Movie Library #

_ITEC2545 final project_

Are you sick and tired of convenient streaming video services that send content directly to your home from the Internet? Fed up with paying a single monthly fee to get all the movies you could ever possibly watch? Are you just _really_ fussy about video compression?

Now, with this application, you can keep a bunch of physical media in your house! Buy extra furniture to store all that crap! Snag those deeply-discounted box sets, and then figure out which one has the movie you want to watch! Maybe even do something useful with all those Ultraviolet codes you've been accumulating!

Technologies: Java, Swing, MySQL, MVC

Requires JRE 1.8, MySQL server

Manually initialize database as follows:

    create user 'lade'@'localhost' identified by 'agram';
    create database movie_library;
    grant select, insert, create, update, drop on movie_library.* to 'lade'@'localhost'; 


