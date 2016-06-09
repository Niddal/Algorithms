This is a small exploration of how competitive the Linear Discriminant Analysis objective (which accounts for class labels)
is over the reconstruction objective of PCA. As a toy example, I create a few randomly parameterized 2D gaussian distributions
and sample a few thousand instances from each. I then compute the LDA principles axes by hand and plot the projections of
the data onto the principle axes to visualize class separation. Depending on how much overlap there is between the
gaussians -- particularly how close the means are -- LDA produces fairly good separation. 

Next, I jump straight into using the build-in LDA functionality, and compare classifiers' performance on the data
projected onto 1) the LDA axes and 2) the PCA axes. I then introduce a new dataset -- the Wisconsin X-ray Microbeam
(XRMB) dataset, which contains two modalities of speech measurements (acoustic and articulatory) with a set of 39
phonemes as labels. 
