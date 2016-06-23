10 CLEAR 49152: REM move stack below top 16k page
20 LET Y = 4
30 PAPER 2:PRINT AT Y,1;" "
40 PAPER 4:PRINT AT Y,2;" "
50 PAPER 6:PRINT AT Y,3;" "
60 PAPER 1:PRINT AT Y,4;" "
70 PAPER 7: INK 0: BRIGHT 0
80 PRINT AT Y,5;" Image to ZX Spectrum "
90 PAPER 2:PRINT AT Y,27;" "
100 PAPER 4:PRINT AT Y,28;" "
110 PAPER 6:PRINT AT Y,29;" "
120 PAPER 1:PRINT AT Y,30;" "
130 LET Y=Y+2
140 PAPER 7: INK 0: BRIGHT 0
150 PRINT AT Y,1;"Copyright Silent Software 2010"
160 LET Y=Y+2
170 PRINT AT Y,1;"* 128K Buffered Video Loader *"
180 LET Y=Y+1
190 PRINT AT Y,1;"Copyright Alcoholics Anonymous"
200 LET Y=Y+2
210 PRINT AT Y,1;"NOTE: You must switch to USR 0"
220 LET Y=Y+1
230 PRINT AT Y,1;"in BASIC before loading this "
240 LET Y=Y+1
250 PRINT AT Y,1;"(type USR 0 to switch to 48K)."
260 LET Y=Y+2
270 PRINT AT Y,1;"Please enter a number to pause"
271 LET Y=Y+1
272 PRINT AT Y,1;"between frames (usually 1 - 50"
273 LET Y=Y+1
274 PRINT AT Y,1;"setting of 0 waits for a key)."
280 INPUT, A
300 PAUSE A: REM sync with raster
310 OUT 32765,23: REM display page 5, put page 7 into top 16k
320 POKE 23739,111
330 LOAD ""CODE 49152: REM load next pic into page 7
340 POKE 23739,244
350 PAUSE A
360 OUT 32765,29: REM display page 7, put page 5 into top 16k
370 POKE 23739,111
380 LOAD ""CODE 49152: REM load next pic into page 5
390 POKE 23739,244
400 GO TO 300