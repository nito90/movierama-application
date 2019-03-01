import React, { Component } from 'react';
import { getAllMovies, getMoviesCreatedByUser, getOpinions, expressOpinion, clearOpinion, getAllMoviesOrderedByLikes, getAllMoviesOrderedByHates } from '../utils/APIClient';
import Movie from './Movie';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon, notification } from 'antd';
import { MOVIES_LIST_SIZE } from '../constants/Constants';
import { withRouter } from 'react-router-dom';
import './movieList.css';
class MovieList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            movies: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            currentVotes: [],
            isLoading: false,
            order: 'date'
        };
        this.loadMovieList = this.loadMovieList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
        this.handleLikeOrdering = this.handleLikeOrdering.bind(this);
        this.handleHateOrdering = this.handleHateOrdering.bind(this);
        this.handleDateOrdering = this.handleDateOrdering.bind(this);

        this.refreshTheOrder = this.refreshTheOrder.bind(this);
    }

    loadMovieList(page = 0, size = MOVIES_LIST_SIZE) {
        let promise;
        if(this.props.username) {
             if(this.props.type === 'USER_CREATED_MOVIES') {
                 promise = getMoviesCreatedByUser(this.props.username, page, size);
             } else if (this.props.type === 'USER_OPINIONS') {
                 promise = getOpinions(this.props.username, page, size);                               
             }
        } else {
            promise = getAllMovies(page, size);
        }

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise            
        .then(response => {
            const movies = this.state.movies.slice();
            const currentVotes = this.state.currentVotes.slice();

            this.setState({
                movies: (page === 0) ? response.content : movies.concat(response.content),
                page: response.page,
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
                last: response.last,
                currentVotes: currentVotes.concat(Array(response.content.length).fill(null)),
                isLoading: false,
                order: 'date'
            })
        }).catch(error => {
            this.setState({
                isLoading: false
            })
        });  
        
    }

    componentDidMount() {
        this.loadMovieList();
    }

    componentDidUpdate(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                movies: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                currentVotes: [],
                isLoading: false,
                order: 'date'
            });    
            this.loadMovieList();
        }
    }

    handleLoadMore() {

        if(this.state.order === 'date'){
            this.loadMovieList(this.state.page + 1);
        }
        else if(this.state.order === 'like'){
            this.loadLikeOrdering(this.state.page + 1);
        }

        else if(this.state.order === 'hate'){
            this.loadHateOrdering(this.state.page + 1);
        }
    }

    refreshTheOrder(){
        if(this.state.order === 'date'){
            this.loadMovieList();
        }
        else if(this.state.order === 'like'){
            this.loadLikeOrdering();
        }

        else if(this.state.order === 'hate'){
            this.loadHateOrdering();
        }
    }

    handleVoteChange(event, movieIndex) {
        const currentVotes = this.state.currentVotes.slice();
        currentVotes[movieIndex] = event.target.value;

        this.setState({
            currentVotes: currentVotes
        });
    }


    handleVoteSubmit(event, movieIndex) {
        event.preventDefault();
        
        if(!this.props.isAuthenticated) {
            this.props.history.push("/login");
            notification.info({
                message: 'Movierama',
                description: "Please login to put an opinion.",          
            });
            return;
        }

        const movie = this.state.movies[movieIndex];
        const selectedChoice = this.state.currentVotes[movieIndex];

        const opinionData = {
            movieId: movie.id,
            opinion: selectedChoice
        };

        expressOpinion(opinionData)
        .then(response => {
            const movies = this.state.movies.slice();
            movies[movieIndex] = response;

            this.setState({
                movies: movies,
                currentVotes: Array(movies.length).fill(null),
            });

            this.refreshTheOrder();

        }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to vote');    
            } else {
                notification.error({
                    message: 'Movierama',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });                
            }
        });
    }

    handleClearVote(event, movieIndex) {
        event.preventDefault();
        if(!this.props.isAuthenticated) {
            this.props.history.push("/login");
            notification.info({
                message: 'Movierama',
                description: "Please login to put an opinion.",          
            });
            return;
        }

        const movie = this.state.movies[movieIndex];
        if(this.props.isProfileMovieList){
            this.props.handleTotalMovieOpinions(this.props.currentUser.username);
        }

        clearOpinion(movie.id)
        .then(response => {
            const movies = this.state.movies.slice();
            movies[movieIndex] = response;

            console.log(this.props.isProfileMovieList);
            console.log(movies[movieIndex].createdBy.username);
            console.log(this.props);
            console.log(this.state);
                
            if((this.props.isProfileMovieList) && ((movies[movieIndex].createdBy.username === this.props.currentUser.username) || (this.props.currentUser.username === this.props.username))){
                
                movies.splice(movieIndex, 1);

            }

            this.setState({
                movies: movies,
                currentVotes: Array(movies.length).fill(null),
            });        
        }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to vote');    
            } else {
                notification.error({
                    message: 'Movierama',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });                
            }
        });
    }

    handleLikeOrdering(){
           this.loadLikeOrdering();
    }

    loadLikeOrdering(page = 0, size = MOVIES_LIST_SIZE){
        let promise;
        promise = getAllMoviesOrderedByLikes(page, size);

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise            
        .then(response => {
            const movies = this.state.movies.slice();
            const currentVotes = this.state.currentVotes.slice();

            this.setState({
                movies: (page === 0) ? response.content : movies.concat(response.content),
                page: response.page,
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
                last: response.last,
                currentVotes: currentVotes.concat(Array(response.content.length).fill(null)),
                isLoading: false,
                order: 'like'
            })
        }).catch(error => {
            this.setState({
                isLoading: false
            })
        });   
    }

    handleHateOrdering(){
        this.loadHateOrdering();
    }

    loadHateOrdering(page = 0, size = MOVIES_LIST_SIZE){
        let promise = getAllMoviesOrderedByHates(page, size);
    
        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise            
        .then(response => {
            const movies = this.state.movies.slice();
            const currentVotes = this.state.currentVotes.slice();

            this.setState({
                movies: (page === 0) ? response.content : movies.concat(response.content),
                page: response.page,
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
                last: response.last,
                currentVotes: currentVotes.concat(Array(response.content.length).fill(null)),
                isLoading: false,
                order: 'hate'
            })
        }).catch(error => {
            this.setState({
                isLoading: false
            })
        }); 
    }

    handleDateOrdering(){
        this.loadMovieList();
    }

    render() {
        const movieViews = [];
        this.state.movies.forEach((movie, movieIndex) => {
            movieViews.push(<Movie 
                key={movie.id} 
                movie={movie}
                currentVote={this.state.currentVotes[movieIndex]} 
                currentUsername={this.props.currentUser ? this.props.currentUser.username : null}
                handleVoteChange={(event) => this.handleVoteChange(event, movieIndex)}
                handleClearVote={(event) => this.handleClearVote(event, movieIndex)}
                handleVoteSubmit={(event) => this.handleVoteSubmit(event, movieIndex)} />)            
        });

        const orderingChoices = [];
        if(!this.props.isProfileMovieList){
            let likeOrderButton = <Button key={"likeOrderButtonKey"} onClick={this.handleLikeOrdering} disabled={this.state.isLoading}>
                    Order by likes<Icon type="like" />
                </Button>
            let hateOrderButton =  <Button key={"hateOrderButtonKey"} onClick={this.handleHateOrdering} disabled={this.state.isLoading}>
                    Order by hates<Icon type="dislike" />
                </Button>
            let dateOrderButton =  <Button key={"dateOrderButtonKey"} onClick={this.handleDateOrdering} disabled={this.state.isLoading}>
                    Order by Date<Icon type="calendar" />
                </Button>
            orderingChoices.push(likeOrderButton);
            orderingChoices.push(hateOrderButton);
            orderingChoices.push(dateOrderButton)

        }
        return (
            <div className="movies-container">

                {orderingChoices}
                {movieViews}
                {
                    !this.state.isLoading && this.state.movies.length === 0 ? (
                        <div className="no-movies-found">
                            <span>No Movies Found.</span>
                        </div>    
                    ): null
                }  
                {
                    !this.state.isLoading && !this.state.last ? (
                        <div className="load-more-movies"> 
                            <Button type="dashed" onClick={this.handleLoadMore} disabled={this.state.isLoading}>
                                <Icon type="plus" /> Load more
                            </Button>
                        </div>): null
                }              
                {
                    this.state.isLoading ? 
                    <LoadingIndicator />: null                     
                }
            </div>
        );
    }
}

export default withRouter(MovieList);