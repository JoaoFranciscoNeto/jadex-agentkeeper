forfiles /s /m *.svg /c "cmd /c inkscape @file --export-png=@fname.png -w40 -h40"
forfiles /s /m *.svg /c "cmd /c echo @file"