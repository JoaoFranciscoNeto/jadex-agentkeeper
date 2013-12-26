forfiles /s /m *.svg /c "cmd /c inkscape @file --export-png=@fname.png -w45 -h45"
forfiles /s /m *.svg /c "cmd /c echo @file"