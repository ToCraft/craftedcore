@echo off
set /p version=Please enter the version (needed for github, also check gradle.properties): 

:: add mcversion here
set mcversions="1.20.2";"1.20.1";"1.19.4"
:: add loader here
set modloader="fabric";"forge"

:: loops
for %%m in (%mcversions%) DO (
    git checkout "%%m"
    gh release create "%version%-%%m" --generate-notes

    .\gradlew publish
    for %%l in (%modloader%) DO (
        call .\gradlew %%l:build
        .\gradlew %%l:modrinth
        .\gradlew %%l:curseforge
        move "%%l\build\libs\craftedcore-1.8-%%l.jar" "%%l\build\craftedcore-%%m-%%l-%version%.jar"
        rmdir /s /q "%%l\build\libs"
        gh release upload "%version%-%%m" "%%l\build\craftedcore-%%m-%%l-%version%.jar"
    )
)
