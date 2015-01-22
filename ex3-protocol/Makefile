

all:
	pdflatex main.tex
	bibtex main
	pdflatex main.tex
	xdg-open main.pdf

clean:
	rm *.aux *.bbl *.blg *.out *.pdf *.log

