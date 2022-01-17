This folder is for storing the json files
of the various paths the robot can follow during autonomous

for the paths, center starts assume you start perfectly lined up with the center of the target
right assumes you start with your center 45 inches from the wall (28 inches from the edge of the robot)
left assumes you start with your center 140 inches from the wall (123 inches from the edge of the robot)



Example:

[

    [0.0, 4.5, 0.2],
    [1.0, -32.7, 0.4],
    [2.0, 60, 24, 0.3]

]

List of commands in order of what the robot would do:

Rotate 4.5 robot rotations at 20% speed

Move 32.7 inches backwards at 40% speed

Make an arc with a chord length of 60 inches and an arc height of 24 inches at 30% speed