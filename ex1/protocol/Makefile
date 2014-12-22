

all:
	pdflatex main.tex
	bibtex main.aux
	pdflatex main.tex
	xdg-open main.pdf
	cp main.pdf ../PROTOCOL.pdf
