cd ../ex3
PDIR=../ex3-protocol/img

function e() {
    VIS=$1
    ID=$2

    bash som.sh chainlink chainlink "--vis $VIS -e $PDIR/cl-$ID.png --flip b"
    bash som.sh chainlink-hex chainlink "--vis $VIS -e $PDIR/cl-$ID-hex.png"
}

function ep() {
    VIS=$1
    ID=$2
    PARAM=$3

    bash som.sh chainlink-hex chainlink "--vis $VIS --visParam $PARAM -e $PDIR/cl-$ID-hex.png"
    bash som.sh chainlink chainlink "--vis $VIS --visParam $PARAM -e $PDIR/cl-$ID.png"
}

e 'ActivityHistogram' 'activity'
#e 'Hit Histogram' 'hit'
#e 'UMatrix' 'umatrix'
#e 'DMatrix' 'dmatrix'
#e 'PMatrix' 'pmatrix'
#e 'QuantizationErr' 'qe'
#e 'Fuzzy Colouring' 'fuzzy-colouring'
#e 'MeanQuantizationErr' 'mqe'
#e 'TopographicError4Units' 'topo-4'
#e 'TopographicError8Units' 'topo-8'
#e 'DistortionSqrt' 'dist'
#ep 'NeighbourhoodKnn' 'nh-knn-0,1' '0.1'
#ep 'NeighbourhoodKnn' 'nh-knn-0,2' '0.2'
#ep 'NeighbourhoodKnn' 'nh-knn-0,3' '0.3'
#ep 'NeighbourhoodRadius' 'nh-radius-0.1' '0.1'
#ep 'NeighbourhoodRadius' 'nh-radius-0.3' '0.2'
#ep 'NeighbourhoodRadius' 'nh-radius-0.2' '0.3'

